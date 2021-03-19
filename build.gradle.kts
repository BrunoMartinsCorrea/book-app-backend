val javaVersion = findProperty("java.version").toString().toInt()
val kotestVersion = findProperty("kotest.version")
val kotlinxCoroutineVersion = findProperty("kotlinx.coroutines.version")
val ktlintVersion = findProperty("ktlint.version")
val logbackVersion = findProperty("logback.version")
val logstashEncoderVersion = findProperty("logstash.encoder.version")
val mockkVersion = findProperty("mockk.version")
val sonarqubeVersion = findProperty("sonarqube.version")

java.sourceCompatibility = JavaVersion.toVersion(javaVersion)

repositories {
    mavenCentral()
    jcenter()
    google()
}

plugins {
    val kotlinPluginVersion = "1.4.31"
    val ktlintPluginVersion = "10.0.0"
    val sonarqubePluginVersion = "3.1.1"

    kotlin("jvm") version kotlinPluginVersion
    jacoco
    id("org.sonarqube") version sonarqubePluginVersion
    id("org.jlleitschuh.gradle.ktlint") version ktlintPluginVersion
}

configurations {
    ktlint
}

subprojects {
    group = "com.company"
    version = "1.0"

    apply(plugin = "kotlin")
    apply(plugin = "jacoco")
    apply(plugin = "org.sonarqube")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

    configurations {
        ktlint
    }

    repositories {
        mavenCentral()
        jcenter()
        google()
    }

    dependencies {
        implementation("ch.qos.logback:logback-classic:$logbackVersion")
        implementation("net.logstash.logback:logstash-logback-encoder:$logstashEncoderVersion")
        implementation("com.pinterest.ktlint:ktlint-core:$ktlintVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutineVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlinxCoroutineVersion")
        implementation("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:$sonarqubeVersion")

        testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
        testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
        testImplementation("io.mockk:mockk:$mockkVersion")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutineVersion")
    }

    sourceSets {
        main {
            java {
                srcDir("src/main/kotlin")
            }
        }
    }

    configurations {
        ktlint
    }

    ktlint {
        debug.set(false)
        outputToConsole.set(true)
        ignoreFailures.set(false)

        filter {
            exclude("**/generated/**")
            exclude("**/java/**")
            include("**/kotlin/**")
        }
    }

    jacoco {
        reportsDirectory.dir("$buildDir/reports/jacoco")
    }

    sonarqube {
        properties {
            property("sonar.jacoco.reportPaths", "${jacoco.reportsDirectory.get()}/coverage.xml")
        }
    }

    tasks {
        build {
            finalizedBy(jacocoTestCoverageVerification)
        }

        compileKotlin {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjvm-default=enable")
                allWarningsAsErrors = true
                jvmTarget = "$javaVersion"
            }
        }

        compileTestKotlin {
            kotlinOptions {
                jvmTarget = "$javaVersion"
            }
        }

        test {
            useJUnitPlatform()
        }

        jacocoTestReport {
            dependsOn(test)

            reports {
                xml.isEnabled = true
                csv.isEnabled = false
                html.isEnabled = false

                xml.destination = file("${jacoco.reportsDirectory.get()}/coverage.xml")
            }
        }

        jacocoTestCoverageVerification {
            dependsOn(jacocoTestReport)

            violationRules {
                rule {
                    element = "CLASS"

                    limit {
                        counter = "LINE"
                        value = "COVEREDRATIO"
                        minimum = 0.8.toBigDecimal()
                    }
                }

                rule {
                    element = "METHOD"

                    limit {
                        counter = "METHOD"
                        value = "COVEREDRATIO"
                        minimum = 1.toBigDecimal()
                    }
                }
            }
        }

        val outputDir = "${project.buildDir}/reports/ktlint/"
        val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/kotlin/**/*.kt"))

        ktlintCheck {
            inputs.files(inputFiles)
            outputs.dir(outputDir)
        }

        ktlintFormat {
            inputs.files(inputFiles)
            outputs.dir(outputDir)
        }
    }
}

tasks {
    this.sonarqube {
        dependsOn(jacocoTestCoverageVerification)
    }
}
