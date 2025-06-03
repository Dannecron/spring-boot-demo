dependencies {
    implementation(project(":edge-contracts"))

    implementation(rootProject.libs.jackson.datatype.jsr)
    implementation(rootProject.libs.jackson.module.kotlin)
    implementation(rootProject.libs.spring.boot.starter.validation)
    implementation(rootProject.libs.spring.cloud.starter.streamKafka)

    testImplementation(rootProject.libs.spring.cloud.streamTestBinder)
}
