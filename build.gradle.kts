plugins {
	alias(libs.plugins.kotlin.jpa)
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependencyManagement)

	jacoco
}

group = "com.github.dannecron.demo"
version = "single-version"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	runtimeOnly(libs.micrometer.registry.prometheus)

	implementation(libs.bundles.tracing)
	implementation(libs.flyway.core)
	implementation(libs.jackson.datatype.jsr)
	implementation(libs.jackson.module.kotlin)
	implementation(libs.json.schema.validator)
	implementation(libs.kotlin.reflect)
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.ktor.client.cio)
	implementation(libs.ktor.client.core)
	implementation(libs.logback.encoder)
	implementation(libs.postgres)
	implementation(libs.spring.aspects)
	implementation(libs.spring.boot.starter.actuator)
	implementation(libs.spring.boot.starter.jdbc)
	implementation(libs.spring.boot.starter.mustache)
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.doc.openapi.starter)
	implementation(libs.spring.kafka)

	testImplementation(libs.kotlin.test.junit)
	testImplementation(libs.mockito.kotlin)
	testImplementation(libs.spring.boot.starter.test)
	testImplementation(libs.spring.kafka.test)
	testImplementation(libs.testcontainers)
	testImplementation(libs.testcontainers.junit.jupiter)
	testImplementation(libs.testcontainers.postgresql)
	testImplementation(libs.ktor.client.mock)

	developmentOnly(libs.spring.boot.devtools)
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
		apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
	dependsOn(tasks.test) // tests are required to run before generating the report
}
