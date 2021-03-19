package com.github.brunomartinscorrea.config

import com.github.brunomartinscorrea.adapter.database.DatabaseMigration
import org.flywaydb.core.Flyway

class DatabaseMigrationImpl(
    private val url: String,
    private val user: String,
    private val password: String,
) : DatabaseMigration {

    override fun migrate() {
        Flyway.configure()
            .locations("classpath:migration")
            .dataSource(
                url,
                user,
                password
            )
            .load()
            .migrate()
    }
}
