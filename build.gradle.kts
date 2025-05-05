plugins {
	idea
	alias(libs.plugins.kotlin.kover)
	alias(libs.plugins.kotlin.jpa)
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.kotlin.serialization)
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependencyManagement)
}

group = "com.github.dannecron.demo"
version = "single-version"

allprojects {
	apply {
		plugin(rootProject.libs.plugins.kotlin.jvm.get().pluginId)
		plugin(rootProject.libs.plugins.kotlin.serialization.get().pluginId)
		plugin(rootProject.libs.plugins.kotlin.kover.get().pluginId)

		plugin("java")
	}

	plugins.withId("org.jetbrains.kotlinx.kover") {
		tasks.named("koverXmlReport") {
			dependsOn(tasks.test)
		}
	}

	java {
		sourceCompatibility = JavaVersion.VERSION_17
	}
	kotlin {
		compilerOptions {
			freeCompilerArgs.addAll("-Xjsr305=strict")
			apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
		}
	}

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation(rootProject.libs.kotlin.reflect)
		implementation(rootProject.libs.kotlinx.serialization.json)
		implementation(rootProject.libs.spring.aspects)

		testImplementation(rootProject.libs.kotlin.test.junit)
		testImplementation(rootProject.libs.mockito.kotlin)
		testImplementation(rootProject.libs.spring.boot.starter.test)

		kover(project(":db"))
	}

	tasks.test {
		useJUnitPlatform()
		finalizedBy("koverXmlReport")
	}
}

subprojects {
	apply {
		plugin(rootProject.libs.plugins.kotlin.spring.get().pluginId)
		plugin(rootProject.libs.plugins.spring.boot.get().pluginId)
		plugin(rootProject.libs.plugins.spring.dependencyManagement.get().pluginId)
	}
}

dependencies {
	implementation(project(":db"))
	implementation(project(":core"))

	runtimeOnly(libs.micrometer.registry.prometheus)

	implementation(libs.bundles.tracing)
	implementation(libs.jackson.datatype.jsr)
	implementation(libs.jackson.module.kotlin)
	implementation(libs.ktor.client.cio)
	implementation(libs.ktor.client.core)
	implementation(libs.logback.encoder)
	implementation(libs.postgres)
	implementation(libs.spring.boot.starter.actuator)
	implementation(libs.spring.boot.starter.jdbc)
	implementation(libs.spring.boot.starter.mustache)
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.cloud.starter.streamKafka)
	implementation(libs.spring.doc.openapi.starter)

	testImplementation(libs.ktor.client.mock)
	testImplementation(libs.spring.boot.starter.actuatorAutoconfigure)
	testImplementation(libs.spring.cloud.starter.streamTestBinder)
	testImplementation(libs.testcontainers)
	testImplementation(libs.testcontainers.junit.jupiter)

	developmentOnly(libs.spring.boot.devtools)
}
