package com.github.brunomartinscorrea.config.environment.module

import com.github.brunomartinscorrea.config.environment.mainConfig

private val httpConfig = mainConfig.getConfig("http")

private val serverConfig = httpConfig.getConfig("server")
val httpServerPort = serverConfig.getInt("port")
val httpServerContextPath = serverConfig.getString("contextPath")!!

private val defaultCircuitBreakerConfig = httpConfig.getConfig("defaultCircuitBreaker")
val defaultCircuitBreakerName = defaultCircuitBreakerConfig.getString("name")!!
val defaultCircuitBreakerWindowSizeCounter = defaultCircuitBreakerConfig.getInt("windowSizeCounter")
val defaultCircuitBreakerFailureRateMinimumNumberOfCalls =
    defaultCircuitBreakerConfig.getInt("failureRateMinimumNumberOfCalls")
val defaultCircuitBreakerFailureRateThreshold = defaultCircuitBreakerConfig.getDouble("failureRateThreshold")
val defaultCircuitBreakerSlowCallDurationThreshold = defaultCircuitBreakerConfig.getLong("slowCallDurationThreshold")
val defaultCircuitBreakerSlowCallRateThreshold = defaultCircuitBreakerConfig.getDouble("slowCallRateThreshold")
val defaultCircuitBreakerOpenStateWaitDuration =
    defaultCircuitBreakerConfig.getLong("openStateWaitDuration")
val defaultCircuitBreakerHalfOpenStatePermittedNumberOfCalls =
    defaultCircuitBreakerConfig.getInt("halfOpenStatePermittedNumberOfCalls")

private val defaultRetryConfig = defaultCircuitBreakerConfig.getConfig("retry")
val defaultCircuitBreakerRetryName = defaultRetryConfig.getString("name")!!
val defaultCircuitBreakerRetryMaxAttempts = defaultRetryConfig.getInt("maxAttempts")
val defaultCircuitBreakerRetryWaitDuration = defaultRetryConfig.getLong("waitDuration")
val defaultCircuitBreakerRetryStatusCodeList: List<Int> = defaultRetryConfig.getIntList("statusCodeList")!!
