package com.github.brunomartinscorrea.config

import com.github.brunomartinscorrea.adapter.database.DatabaseApi
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DatabaseApiImpl(
    driver: String,
    url: String,
    user: String,
    password: String,
    poolSize: Int,
    autoCommit: Boolean
) : DatabaseApi {

    init {
        HikariConfig().apply {
            driverClassName = driver
            jdbcUrl = url
            username = user
            this.password = password
            maximumPoolSize = poolSize
            isAutoCommit = autoCommit
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }.let { config ->
            HikariDataSource(config)
        }.let { dataSource ->
            Database.connect(dataSource)
        }
    }

    override suspend fun <T> transaction(function: () -> T): T = newSuspendedTransaction(Dispatchers.IO) {
        function()
    }
}
