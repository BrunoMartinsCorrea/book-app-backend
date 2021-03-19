package com.github.brunomartinscorrea.config.environment

import com.typesafe.config.ConfigFactory

internal val defaultConfig = ConfigFactory.load("application-default")
internal val mainConfig = ConfigFactory.load().withFallback(defaultConfig)
