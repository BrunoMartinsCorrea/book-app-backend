package com.github.brunomartinscorrea.config.environment.module

import com.github.brunomartinscorrea.config.environment.mainConfig

private val databaseConfig = mainConfig.getConfig("database")
val databaseDriver = databaseConfig.getString("driver")!!
val databaseUrl = databaseConfig.getString("url")!!
val databaseUser = databaseConfig.getString("user")!!
val databasePassword = databaseConfig.getString("password")!!
val databasePoolSize = databaseConfig.getInt("poolSize")
val databaseAutoCommit = databaseConfig.getBoolean("autoCommit")
