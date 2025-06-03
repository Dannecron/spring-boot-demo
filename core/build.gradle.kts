group = "com.github.dannecron.demo"
version = "single-version"

dependencies {
    implementation(project(":db"))

    implementation(rootProject.libs.spring.boot.starter.actuator)
    implementation(rootProject.libs.spring.boot.starter.jdbc)
    implementation(rootProject.libs.spring.boot.starter.validation)
    implementation(rootProject.libs.json.schema.validator)

    testImplementation(rootProject.libs.spring.boot.starter.actuatorAutoconfigure)
}
