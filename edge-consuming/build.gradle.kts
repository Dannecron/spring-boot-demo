dependencies {
    implementation(project(":edge-contracts"))
    implementation(project(":core"))

    implementation(rootProject.libs.com.fasterxml.jackson.datatype.jackson.datatype.jsr310)
    implementation(rootProject.libs.com.fasterxml.jackson.module.jackson.module.kotlin)
    implementation(rootProject.libs.org.springframework.cloud.spring.cloud.starter.stream.kafka)
    implementation(rootProject.libs.org.springframework.cloud.spring.cloud.stream)

    testImplementation(rootProject.libs.org.springframework.cloud.spring.cloud.stream.test.binder)
}
