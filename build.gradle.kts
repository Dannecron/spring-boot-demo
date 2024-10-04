plugins {
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"

	jacoco

	kotlin("jvm") version "2.0.20"
	kotlin("plugin.jpa") version "1.9.23"
	kotlin("plugin.serialization") version "2.0.20"
	kotlin("plugin.spring") version "1.9.23"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	api("org.springframework.boot:spring-boot-starter-data-jdbc:3.2.4")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.4")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.4")
	implementation("io.github.optimumcode:json-schema-validator:0.2.3")
	implementation("org.flywaydb:flyway-core:9.22.3")
	implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.20")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
	implementation("org.postgresql:postgresql:42.6.2")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	implementation("org.springframework.boot:spring-boot-starter-mustache:3.2.4")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.2.4")
	implementation("org.springframework.boot:spring-boot-starter-web:3.2.4")
	implementation("org.springframework.kafka:spring-kafka:3.1.3")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.0.20")
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.4")
	testImplementation("org.springframework.kafka:spring-kafka-test:3.1.3")
	testImplementation("org.testcontainers:junit-jupiter:1.19.7")
	testImplementation("org.testcontainers:testcontainers:1.19.7")
	testImplementation("org.testcontainers:postgresql:1.19.7")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
		apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_7)
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

