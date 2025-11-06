dependencies {
    implementation(project(":edge-contracts"))

    implementation(rootProject.libs.com.fasterxml.jackson.datatype.jackson.datatype.jsr310)
    implementation(rootProject.libs.com.fasterxml.jackson.module.jackson.module.kotlin)
    implementation(rootProject.libs.org.springframework.boot.spring.boot.starter.validation)
    implementation(rootProject.libs.org.springframework.cloud.spring.cloud.starter.stream.kafka)

    testImplementation(rootProject.libs.org.springframework.cloud.spring.cloud.stream.test.binder)
}
