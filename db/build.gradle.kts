plugins {
    alias(libs.plugins.kotlin.jpa)
}

dependencies {
    implementation(rootProject.libs.flyway.core)
    implementation(rootProject.libs.postgres)
    implementation(rootProject.libs.springBoot.starter.jdbc)

    testImplementation(libs.testcontainers)
    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.postgresql)
}
