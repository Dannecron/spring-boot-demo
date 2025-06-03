dependencies {
    implementation(project(":edge-contracts"))

    implementation(rootProject.libs.jackson.datatype.jsr)
    implementation(rootProject.libs.jackson.module.kotlin)
    implementation(rootProject.libs.springBoot.starter.validation)
    implementation(rootProject.libs.springCloud.starter.streamKafka)

    testImplementation(rootProject.libs.springCloud.streamTestBinder)
}
