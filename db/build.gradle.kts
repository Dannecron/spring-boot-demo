plugins {
    alias(libs.plugins.org.jetbrains.kotlin.plugin.jpa)
}

dependencies {
    implementation(rootProject.libs.org.flywaydb.flyway.core)
    implementation(rootProject.libs.org.postgresql.postgresql)
    implementation(rootProject.libs.org.springframework.boot.spring.boot.starter.data.jdbc)

    testImplementation(libs.org.testcontainers.junit.jupiter)
    testImplementation(libs.org.testcontainers.postgresql)
    testImplementation(libs.org.testcontainers.testcontainers)
}
