dependencies {
    implementation(project(":edge-contracts"))
    implementation(project(":core"))

    implementation(rootProject.libs.jackson.datatype.jsr)
    implementation(rootProject.libs.jackson.module.kotlin)
    implementation(rootProject.libs.springCloud.starter.streamKafka)
    implementation(rootProject.libs.springCloud.stream)

    testImplementation(rootProject.libs.springCloud.streamTestBinder)
}
