val configVersion: String by project
val koinVersion: String by project
val kotestVersion: String by project
val kotestKoinVersion: String by project
val logstashEncoderVersion: String by project

plugins {
    application
}

application {
    mainClass.set("com.github.brunomartinscorrea.Application")

    applicationDefaultJvmArgs = listOf(
        "-server",
        "-XX:+UseNUMA",
        "-XX:+UseParallelGC",
        "-Duser.timezone=America/Sao_Paulo"
    )
}

dependencies {
    implementation(project(":database"))
    implementation(project(":entity"))
    implementation(project(":http"))
    implementation(project(":service"))

    implementation("net.logstash.logback:logstash-logback-encoder:$logstashEncoderVersion")
    implementation("com.typesafe:config:$configVersion")
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")

    testImplementation("io.insert-koin:koin-test:$koinVersion")
    testImplementation("io.kotest.extensions:kotest-extensions-koin:$kotestKoinVersion")
}

tasks {
    jar {
        archiveBaseName.set("app")
        archiveAppendix.set("")
        archiveVersion.set("")
        archiveClassifier.set("")

        manifest {
            attributes(mapOf("Main-Class" to application.mainClass))
        }

        from(
            Callable {
                duplicatesStrategy = DuplicatesStrategy.INCLUDE
                isZip64 = true
                configurations["runtimeClasspath"].map { if (it.isDirectory) it else zipTree(it) }
            }
        )
    }
}
