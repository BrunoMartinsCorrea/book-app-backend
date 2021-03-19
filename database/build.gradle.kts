val exposedVersion = findProperty("exposed.version")
val flywayVersion = findProperty("flyway.version")
val hikariVersion = findProperty("hikari.version")
val kotlinVersion = findProperty("kotlin.version")
val postgresqlVersion = findProperty("postgresql.version")

plugins {
    val flywayPluginVersion = "7.6.0"

    id("org.flywaydb.flyway") version flywayPluginVersion
}

dependencies {
    implementation(project(":entity"))

    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("com.zaxxer:HikariCP:$hikariVersion")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.flywaydb:flyway-core:$flywayVersion")
}

flyway {
    url = System.getenv("DB_URL")
    user = System.getenv("DB_USER")
    password = System.getenv("DB_PASSWORD")
    baselineOnMigrate = true
    locations = arrayOf("classpath:db/migration")
}
