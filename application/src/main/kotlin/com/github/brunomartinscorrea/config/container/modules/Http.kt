package com.github.brunomartinscorrea.config.container.modules

import com.github.brunomartinscorrea.adapter.http.CircuitBreaker
import com.github.brunomartinscorrea.adapter.http.Controller
import com.github.brunomartinscorrea.adapter.http.HttpClient
import com.github.brunomartinscorrea.adapter.http.HttpServer
import com.github.brunomartinscorrea.config.client.CircuitBreakerImpl
import com.github.brunomartinscorrea.config.client.HttpClientImpl
import com.github.brunomartinscorrea.config.environment.module.defaultCircuitBreakerFailureRateMinimumNumberOfCalls
import com.github.brunomartinscorrea.config.environment.module.defaultCircuitBreakerFailureRateThreshold
import com.github.brunomartinscorrea.config.environment.module.defaultCircuitBreakerHalfOpenStatePermittedNumberOfCalls
import com.github.brunomartinscorrea.config.environment.module.defaultCircuitBreakerName
import com.github.brunomartinscorrea.config.environment.module.defaultCircuitBreakerOpenStateWaitDuration
import com.github.brunomartinscorrea.config.environment.module.defaultCircuitBreakerRetryMaxAttempts
import com.github.brunomartinscorrea.config.environment.module.defaultCircuitBreakerRetryName
import com.github.brunomartinscorrea.config.environment.module.defaultCircuitBreakerRetryStatusCodeList
import com.github.brunomartinscorrea.config.environment.module.defaultCircuitBreakerRetryWaitDuration
import com.github.brunomartinscorrea.config.environment.module.defaultCircuitBreakerSlowCallDurationThreshold
import com.github.brunomartinscorrea.config.environment.module.defaultCircuitBreakerSlowCallRateThreshold
import com.github.brunomartinscorrea.config.environment.module.defaultCircuitBreakerWindowSizeCounter
import com.github.brunomartinscorrea.config.environment.module.httpServerContextPath
import com.github.brunomartinscorrea.config.environment.module.httpServerPort
import com.github.brunomartinscorrea.config.server.HttpServerImpl
import com.github.brunomartinscorrea.health.HealthControllerImpl
import com.github.brunomartinscorrea.health.HealthDto
import com.github.brunomartinscorrea.root.RootControllerImpl
import com.github.brunomartinscorrea.user.UserControllerImpl
import com.github.brunomartinscorrea.user.UserDto
import java.io.IOException
import java.net.ConnectException
import java.util.concurrent.TimeoutException
import org.koin.core.qualifier.named
import org.koin.dsl.module

val httpModules = module {
    single<Controller<String>>(named("rootController")) { RootControllerImpl() }
    single<Controller<HealthDto>>(named("healthController")) { HealthControllerImpl() }
    single<Controller<UserDto>>(named("userController")) { UserControllerImpl(get()) }

    single<HttpServer> {
        HttpServerImpl(
            port = httpServerPort,
            contextPath = httpServerContextPath,
            rootController = get(named("rootController")),
            healthController = get(named("healthController")),
            userController = get(named("userController"))
        )
    }

    single<CircuitBreaker<*>> {
        CircuitBreakerImpl(
            circuitBreakerName = defaultCircuitBreakerName,
            windowSizeCounter = defaultCircuitBreakerWindowSizeCounter,
            failureRateMinimumNumberOfCalls = defaultCircuitBreakerFailureRateMinimumNumberOfCalls,
            failureRateThreshold = defaultCircuitBreakerFailureRateThreshold.toFloat(),
            slowCallDurationThreshold = defaultCircuitBreakerSlowCallDurationThreshold,
            slowCallRateThreshold = defaultCircuitBreakerSlowCallRateThreshold.toFloat(),
            openStateWaitDuration = defaultCircuitBreakerOpenStateWaitDuration,
            halfOpenStatePermittedNumberOfCalls = defaultCircuitBreakerHalfOpenStatePermittedNumberOfCalls,
            retryName = defaultCircuitBreakerRetryName,
            retryMaxAttempts = defaultCircuitBreakerRetryMaxAttempts,
            retryWaitDuration = defaultCircuitBreakerRetryWaitDuration,
            retryStatusCodeList = defaultCircuitBreakerRetryStatusCodeList,
            exceptionClasses = arrayOf(
                IOException::class.java,
                TimeoutException::class.java,
                ConnectException::class.java
            )
        )
    }

    single<HttpClient> { HttpClientImpl(get()) }
}
