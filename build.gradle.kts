val javaVersion: String by project
val kotestVersion: String by project
val kotlinxCoroutinesVersion: String by project
val logbackVersion: String by project
val mockkVersion: String by project

plugins {
    // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
    val kotlinPluginVersion = "1.6.21"
    // https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint
    val ktlintPluginVersion = "10.3.0"
    // https://plugins.gradle.org/plugin/org.sonarqube
    val sonarqubePluginVersion = "3.3"

    kotlin("jvm") version kotlinPluginVersion
    jacoco
    id("org.sonarqube") version sonarqubePluginVersion
    id("org.jlleitschuh.gradle.ktlint") version ktlintPluginVersion
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }

    apply {
        plugin("kotlin")
        plugin("jacoco")
        plugin("org.sonarqube")
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    dependencies {
        implementation("ch.qos.logback:logback-classic:$logbackVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$kotlinxCoroutinesVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlinxCoroutinesVersion")

        testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
        testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
        testImplementation("io.mockk:mockk:$mockkVersion")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesVersion")
    }

    group = "com.company"
    version = "1.0"

    java.sourceCompatibility = JavaVersion.toVersion(javaVersion)
    java.targetCompatibility = JavaVersion.toVersion(javaVersion)

    sourceSets {
        main { java.srcDirs(listOf("src/main/kotlin")) }
        test { java.srcDirs(listOf("src/test/kotlin")) }
    }

    configurations {
        ktlint
    }

    ktlint {
        additionalEditorconfigFile.set(projectDir.resolve(".editorconfig"))
        android.set(false)
        debug.set(false)
        disabledRules.set(setOf())
        enableExperimentalRules.set(false)
        ignoreFailures.set(false)
        outputColorName.set("RED")
        outputToConsole.set(true)
        verbose.set(true)

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

    reporting {
        baseDir = layout.buildDirectory.dir("report").get().asFile
    }

    tasks {
        val compilerArgs = listOf(
            "-Xjsr305=strict",
            "-Xjvm-default=all",
        )
        val outputDir = "${project.buildDir}/reports/ktlint/"
        val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/kotlin/**/*.kt"))

        compileKotlin {
            kotlinOptions {
                freeCompilerArgs = compilerArgs
                allWarningsAsErrors = true
                jvmTarget = javaVersion
            }
        }

        compileTestKotlin {
            kotlinOptions {
                jvmTarget = javaVersion
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
                enabled = false

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

        build {
            finalizedBy(jacocoTestCoverageVerification)
        }

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
