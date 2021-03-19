val configVersion = findProperty("config.version")
val koinVersion = findProperty("koin.version")
val kotestVersion = findProperty("kotest.version")

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

    implementation("com.typesafe:config:$configVersion")
    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.koin:koin-logger-slf4j:$koinVersion")

    testImplementation("org.koin:koin-test:$koinVersion")
    testImplementation("io.kotest:kotest-extensions-koin:$kotestVersion")
}

tasks {
    jar {
        archiveBaseName.set("app")
        archiveVersion.set("")

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
