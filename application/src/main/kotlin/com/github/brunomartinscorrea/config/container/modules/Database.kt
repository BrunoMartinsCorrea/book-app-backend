package com.github.brunomartinscorrea.config.container.modules

import com.github.brunomartinscorrea.adapter.database.DatabaseApi
import com.github.brunomartinscorrea.adapter.database.DatabaseMigration
import com.github.brunomartinscorrea.adapter.database.UserRepository
import com.github.brunomartinscorrea.config.DatabaseApiImpl
import com.github.brunomartinscorrea.config.DatabaseMigrationImpl
import com.github.brunomartinscorrea.config.environment.module.databaseAutoCommit
import com.github.brunomartinscorrea.config.environment.module.databaseDriver
import com.github.brunomartinscorrea.config.environment.module.databasePassword
import com.github.brunomartinscorrea.config.environment.module.databasePoolSize
import com.github.brunomartinscorrea.config.environment.module.databaseUrl
import com.github.brunomartinscorrea.config.environment.module.databaseUser
import com.github.brunomartinscorrea.user.UserRepositoryImpl
import org.koin.dsl.module

val databaseModules = module {
    single<DatabaseApi> {
        DatabaseApiImpl(
            databaseDriver,
            databaseUrl,
            databaseUser,
            databasePassword,
            databasePoolSize,
            databaseAutoCommit
        )
    }

    single<DatabaseMigration> {
        DatabaseMigrationImpl(
            databaseUrl,
            databaseUser,
            databasePassword
        )
    }

    single<UserRepository> { UserRepositoryImpl(get()) }
}
