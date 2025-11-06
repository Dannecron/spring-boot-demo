import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
	idea
	alias(libs.plugins.io.spring.dependency.management)
	alias(libs.plugins.org.jetbrains.kotlin.jvm)
	alias(libs.plugins.org.jetbrains.kotlin.plugin.jpa)
	alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
	alias(libs.plugins.org.jetbrains.kotlin.plugin.spring)
	alias(libs.plugins.org.jetbrains.kotlinx.kover)
	alias(libs.plugins.org.springframework.boot)
}

group = "com.github.dannecron.demo"
version = "single-version"

allprojects {
	apply {
		plugin(rootProject.libs.plugins.org.jetbrains.kotlin.jvm.get().pluginId)
		plugin(rootProject.libs.plugins.org.jetbrains.kotlin.plugin.serialization.get().pluginId)
		plugin(rootProject.libs.plugins.org.jetbrains.kotlinx.kover.get().pluginId)
		plugin(rootProject.libs.plugins.org.springframework.boot.get().pluginId)

		plugin("java")
	}

	plugins.withId(rootProject.libs.plugins.org.jetbrains.kotlinx.kover.get().pluginId) {
		tasks.named("koverXmlReport") {
			dependsOn(tasks.test)
		}
	}

	java {
		sourceCompatibility = JavaVersion.VERSION_20
		targetCompatibility = JavaVersion.VERSION_20
	}

	kotlin {
		compilerOptions {
			jvmTarget.set(JvmTarget.JVM_20)
			freeCompilerArgs.addAll("-Xjsr305=strict")
			apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
		}
	}

	repositories {
		mavenCentral()
	}

	dependencies {
		runtimeOnly(rootProject.libs.io.micrometer.micrometer.registry.prometheus)

		implementation(rootProject.libs.bundles.tracing)
		implementation(rootProject.libs.net.logstash.logback.logstash.logback.encoder)
		implementation(rootProject.libs.org.jetbrains.kotlin.kotlin.reflect)
		implementation(rootProject.libs.org.jetbrains.kotlinx.kotlinx.serialization.json)
		implementation(rootProject.libs.org.springframework.spring.aspects)

		testImplementation(rootProject.libs.org.jetbrains.kotlin.kotlin.test.junit5)
		testImplementation(rootProject.libs.org.mockito.kotlin.mockito.kotlin)
		testImplementation(rootProject.libs.org.springframework.boot.spring.boot.starter.test)
	}

	tasks.test {
		useJUnitPlatform()
		finalizedBy("koverXmlReport")

		jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED", "--add-opens=java.base/java.util=ALL-UNNAMED")
	}
}

subprojects {
	apply {
		plugin(rootProject.libs.plugins.io.spring.dependency.management.get().pluginId)
		plugin(rootProject.libs.plugins.org.jetbrains.kotlin.plugin.spring.get().pluginId)
		plugin(rootProject.libs.plugins.org.springframework.boot.get().pluginId)
	}

	tasks.bootJar {
		enabled = false
	}
}

dependencies {
	implementation(project(":edge-contracts"))
	implementation(project(":db"))
	implementation(project(":edge-producing"))
	implementation(project(":edge-integration"))
	implementation(project(":core"))
	implementation(project(":edge-consuming"))
	implementation(project(":edge-rest"))

	implementation(libs.org.springframework.boot.spring.boot.starter.mustache)
	implementation(libs.org.springframework.boot.spring.boot.starter.web)

	testImplementation(libs.com.tngtech.archunit.archunit.junit5)

	developmentOnly(libs.org.springframework.boot.spring.boot.devtools)

	kover(project(":edge-contracts"))
	kover(project(":db"))
	kover(project(":edge-producing"))
	kover(project(":edge-integration"))
	kover(project(":core"))
	kover(project(":edge-consuming"))
	kover(project(":edge-rest"))
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
