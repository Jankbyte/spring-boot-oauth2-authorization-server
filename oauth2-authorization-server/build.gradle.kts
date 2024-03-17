import org.springframework.boot.gradle.tasks.run.BootRun
import ru.jankbyte.environmentproperty.FilePropertyLoader
import ru.jankbyte.environmentproperty.EnvironmentPropertyLoader

val jdkVersion: String by project
val propertiesDirPath: String = "${project.rootDir}/env-props"

plugins {
    java
    id("org.springframework.boot") version "3.2.3"
}

apply(plugin = "io.spring.dependency-management")

repositories {
    mavenCentral()
}

configurations.implementation {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

dependencies {
    implementation("org.springframework.session:spring-session-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-jetty")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-authorization-server") {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.seleniumhq.selenium:selenium-java")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    runtimeOnly("org.liquibase:liquibase-core")
    runtimeOnly("org.postgresql:postgresql")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(jdkVersion))
        vendor.set(JvmVendorSpec.ORACLE)
    }
}

tasks.named<BootRun>("bootRun") {
    // jvmArgs("-Dcom.sun.jndi.ldap.object.disableEndpointIdentification=true")
    val activeProfiles = "dev"
    systemProperty("spring.profiles.active", activeProfiles)
    val propsLoader: EnvironmentPropertyLoader = FilePropertyLoader(
        propertiesDirPath, listOf("all", activeProfiles))
    propsLoader.getProperties().forEach { (key, value) ->
        systemProperty(key, value)
    }
}

tasks.named<Test>("test") {
    val propsLoader: EnvironmentPropertyLoader = FilePropertyLoader(
        propertiesDirPath, listOf("all", "test"))
    propsLoader.getProperties().forEach { (key, value) ->
        systemProperty(key, value)
    }
    useJUnitPlatform()
}

tasks.named("clean") {
    doFirst {
        val logsDirPath : String = "${project.projectDir}/logs"
        logger.info("Removing folder: {}", logsDirPath)
        project.delete(logsDirPath)
    }
}
