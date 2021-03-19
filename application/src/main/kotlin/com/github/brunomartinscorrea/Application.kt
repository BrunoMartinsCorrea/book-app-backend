package com.github.brunomartinscorrea

import com.github.brunomartinscorrea.adapter.database.DatabaseMigration
import com.github.brunomartinscorrea.adapter.http.HttpServer
import com.github.brunomartinscorrea.config.container.Container
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

class Application {
    companion object {
        private val logger = LoggerFactory.getLogger(Application::class.java)

        @JvmStatic
        fun main(args: Array<String>) = runBlocking {
            try {
                Container.start()
                val databaseMigration = Container.getInstanceOf<DatabaseMigration>()
                val httpServer = Container.getInstanceOf<HttpServer>()

                databaseMigration.migrate()

                httpServer.start()
            } catch (ex: Exception) {
                logger.error(ex.message, ex)
            }
        }
    }
}
