group = "com.github.dannecron.demo"
version = "single-version"

dependencies {
    implementation(project(":core"))

    implementation(rootProject.libs.jackson.datatype.jsr)
    implementation(rootProject.libs.jackson.module.kotlin)
    implementation(rootProject.libs.spring.cloud.starter.streamKafka)
    implementation(rootProject.libs.spring.cloud.stream)

    testImplementation(rootProject.libs.spring.cloud.streamTestBinder)
}
