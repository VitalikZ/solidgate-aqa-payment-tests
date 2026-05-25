plugins {
    java
    id("io.qameta.allure") version "4.0.2"
}

group = "com.solidgate.aqa"
version = "1.0.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

configurations.all {
    resolutionStrategy {
        force("org.aspectj:aspectjweaver:1.9.24")
    }
}

val selenideVersion = "7.9.4"
val testngVersion = "7.11.0"
val restAssuredVersion = "5.5.5"
val allureVersion = "2.29.1"
val jacksonVersion = "2.18.2"
val lombokVersion = "1.18.38"
val assertjVersion = "3.27.3"
val slf4jVersion = "2.0.16"
val logbackVersion = "1.5.16"

dependencies {
    implementation("com.codeborne:selenide:$selenideVersion")
    implementation("io.rest-assured:rest-assured:$restAssuredVersion")
    implementation("io.rest-assured:json-path:$restAssuredVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")

    implementation("io.qameta.allure:allure-java-commons:$allureVersion")
    implementation("io.qameta.allure:allure-rest-assured:$allureVersion")
    implementation("io.qameta.allure:allure-selenide:$allureVersion")
    implementation("org.assertj:assertj-core:$assertjVersion")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")

    testImplementation("org.testng:testng:$testngVersion")
    testImplementation("io.qameta.allure:allure-testng:$allureVersion")
}

tasks.test {
    useTestNG {
        val suiteFile = project.findProperty("suiteFile") as String?
        if (suiteFile != null) {
            suites(file(suiteFile))
        } else {
            suites(file("src/test/resources/suites/testng.xml"))
        }
        useDefaultListeners = false
    }

    systemProperty("browser", System.getProperty("browser", "chrome"))
    systemProperty("headless", System.getProperty("headless", "false"))
    systemProperty("timeout", System.getProperty("timeout", "10000"))
    systemProperty("browserSize", System.getProperty("browserSize", "1920x1080"))
    systemProperty("allure.results.directory", "build/allure-results")

    val dotenv = file(".env")
    if (dotenv.exists()) {
        dotenv.readLines()
            .map { it.trim() }
            .filter { it.isNotEmpty() && !it.startsWith("#") }
            .forEach { line ->
                val idx = line.indexOf('=')
                if (idx > 0) {
                    val key = line.substring(0, idx).trim()
                    val value = line.substring(idx + 1).trim()
                        .removeSurrounding("\"")
                        .removeSurrounding("'")
                    environment(key, value)
                }
            }
    }
    listOf("SOLIDGATE_MERCHANT_ID", "SOLIDGATE_SECRET_KEY",
           "SOLIDGATE_API_BASE_URL", "SOLIDGATE_PAYMENT_PAGE_BASE_URL").forEach { key ->
        System.getenv(key)?.let { environment(key, it) }
    }

    testLogging {
        events("passed", "failed", "skipped")
        showStandardStreams = true
        showExceptions = true
        showCauses = true
        showStackTraces = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }

    reports {
        html.required = true
        junitXml.required = true
    }
}

tasks.register("printDependencies") {
    doLast {
        configurations.testRuntimeClasspath.get().forEach { println(it) }
    }
}
