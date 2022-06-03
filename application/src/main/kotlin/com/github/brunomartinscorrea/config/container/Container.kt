package com.github.brunomartinscorrea.config.container

import com.github.brunomartinscorrea.config.container.modules.databaseModules
import com.github.brunomartinscorrea.config.container.modules.httpModules
import com.github.brunomartinscorrea.config.container.modules.serviceModules
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level.INFO
import org.koin.logger.slf4jLogger

object Container {
    fun start() = startKoin {
        slf4jLogger(INFO)

        modules(
            listOf(
                databaseModules,
                httpModules,
                serviceModules
            )
        )
    }

    inline fun <reified T> getInstanceOf() = GlobalContext.get().get<T>(T::class)
}
