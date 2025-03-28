plugins {
    alias(libs.plugins.kotlin.jpa)
}

group = "com.github.dannecron.demo"
version = "single-version"

dependencies {
    implementation(rootProject.libs.flyway.core)
    implementation(rootProject.libs.postgres)
    implementation(rootProject.libs.spring.boot.starter.jdbc)

    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.postgresql)
}
