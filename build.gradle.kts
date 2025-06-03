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
		plugin(rootProject.libs.plugins.spring.boot.get().pluginId)

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
		runtimeOnly(rootProject.libs.micrometer.registry.prometheus)

		implementation(rootProject.libs.bundles.tracing)
		implementation(rootProject.libs.kotlin.reflect)
		implementation(rootProject.libs.kotlinx.serialization.json)
		implementation(rootProject.libs.logback.encoder)
		implementation(rootProject.libs.spring.aspects)

		testImplementation(rootProject.libs.kotlin.test.junit)
		testImplementation(rootProject.libs.mockito.kotlin)
		testImplementation(rootProject.libs.spring.boot.starter.test)
	}

	tasks.test {
		useJUnitPlatform()
		finalizedBy("koverXmlReport")

		jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED", "--add-opens=java.base/java.util=ALL-UNNAMED")
	}
}

subprojects {
	apply {
		plugin(rootProject.libs.plugins.kotlin.spring.get().pluginId)
		plugin(rootProject.libs.plugins.spring.boot.get().pluginId)
		plugin(rootProject.libs.plugins.spring.dependencyManagement.get().pluginId)
	}

	tasks.bootJar {
		enabled = false
	}
}

dependencies {
	implementation(project(":db"))
	implementation(project(":core"))
	implementation(project(":edge-consuming"))

	implementation(libs.jackson.datatype.jsr)
	implementation(libs.jackson.module.kotlin)
	implementation(libs.ktor.client.cio)
	implementation(libs.ktor.client.core)
	implementation(libs.postgres)
	implementation(libs.spring.boot.starter.jdbc)
	implementation(libs.spring.boot.starter.mustache)
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.cloud.starter.streamKafka)
	implementation(libs.spring.cloud.stream)
	implementation(libs.spring.doc.openapi.starter)

	testImplementation(libs.ktor.client.mock)
	testImplementation(libs.spring.cloud.streamTestBinder)
	testImplementation(libs.testcontainers)
	testImplementation(libs.testcontainers.junit.jupiter)

	developmentOnly(libs.spring.boot.devtools)

	kover(project(":edge-consuming"))
	kover(project(":core"))
	kover(project(":db"))
}

tasks.bootJar {
	enabled = true
	archiveFileName.set("app.jar")
	mainClass.set("com.github.dannecron.demo.DemoApplicationKt")
}

tasks.jar {
	enabled = false
}

springBoot {
	buildInfo()
}
