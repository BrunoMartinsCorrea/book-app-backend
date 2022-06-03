package com.github.brunomartinscorrea.config.client

import com.github.brunomartinscorrea.adapter.http.CircuitBreaker
import com.github.brunomartinscorrea.exception.http.CircuitBreakerException
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.kotlin.circuitbreaker.circuitBreaker
import io.github.resilience4j.kotlin.retry.retry
import io.github.resilience4j.retry.Retry
import io.github.resilience4j.retry.RetryConfig
import io.github.resilience4j.retry.RetryRegistry
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse
import java.time.Duration.ofMillis
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import org.slf4j.LoggerFactory

class CircuitBreakerImpl(
    circuitBreakerName: String,
    windowSizeCounter: Int,
    failureRateMinimumNumberOfCalls: Int,
    failureRateThreshold: Float,
    slowCallDurationThreshold: Long,
    slowCallRateThreshold: Float,
    openStateWaitDuration: Long,
    halfOpenStatePermittedNumberOfCalls: Int,
    retryName: String,
    retryMaxAttempts: Int,
    retryWaitDuration: Long,
    retryStatusCodeList: List<Int>,
    exceptionClasses: Array<Class<out Throwable>>
) : CircuitBreaker<HttpResponse> {

    init {
        val circuitBreakerConfig = CircuitBreakerConfig.custom()
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(windowSizeCounter)
            .minimumNumberOfCalls(failureRateMinimumNumberOfCalls)
            .failureRateThreshold(failureRateThreshold)
            .slowCallDurationThreshold(ofMillis(slowCallDurationThreshold))
            .slowCallRateThreshold(slowCallRateThreshold)
            .waitDurationInOpenState(ofMillis(openStateWaitDuration))
            .permittedNumberOfCallsInHalfOpenState(halfOpenStatePermittedNumberOfCalls)
            .automaticTransitionFromOpenToHalfOpenEnabled(true)
            .build()

        val circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig)
            .apply {
                eventPublisher.onEntryAdded {
                    logger.warn("Circuit Breaker Registry Entry Added")
                }.onEntryReplaced {
                    logger.warn("Circuit Breaker Registry Entry Replaced")
                }.onEntryRemoved {
                    logger.warn("Circuit Breaker Registry Entry Removed")
                }
            }

        circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName, circuitBreakerConfig)
            .apply {
                eventPublisher.onStateTransition {
                    logger.warn("Circuit Breaker Transition State -> ${it.stateTransition.toState}")
                }.onSlowCallRateExceeded {
                    logger.warn("Circuit Breaker Slow Call Rate Exceeded -> ${it.slowCallRate}")
                }.onError {
                    logger.error("Circuit Breaker Error -> ${it.throwable.message}", it.throwable)
                }
            }

        val retryConfig = RetryConfig.custom<HttpResponse>()
            .maxAttempts(retryMaxAttempts)
            .waitDuration(ofMillis(retryWaitDuration))
            .retryOnResult { it.status.value in retryStatusCodeList }
            .retryExceptions(*exceptionClasses.plus(ClientRequestException::class.java))
            .build()

        retry = RetryRegistry.of(retryConfig)
            .retry(retryName)
            .apply {
                eventPublisher.onRetry {
                    logger.warn("Retry -> ${it.numberOfRetryAttempts}")
                }.onIgnoredError {
                    logger.warn("Retry Ignored Error -> ${it.lastThrowable.message}", it.lastThrowable)
                }.onError {
                    logger.error("Retry Error -> ${it.lastThrowable.message}", it.lastThrowable)
                }
            }
    }

    override suspend fun apply(function: suspend () -> HttpResponse) = flow {
        emit(function())
    }
        .retry(retry)
        .circuitBreaker(circuitBreaker)
        .catch {
            circuitBreaker.transitionToOpenState()
            logger.error("Flow Error -> ${it.message}", it.cause)
            throw CircuitBreakerException()
        }.first()

    companion object {
        private lateinit var circuitBreaker: io.github.resilience4j.circuitbreaker.CircuitBreaker
        private lateinit var retry: Retry
        private val logger = LoggerFactory.getLogger(CircuitBreakerImpl::class.java)
    }
}
