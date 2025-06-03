group = "com.github.dannecron.demo"
version = "single-version"

dependencies {
    implementation(project(":db"))
    implementation(project(":edge-producing"))

    implementation(rootProject.libs.spring.boot.starter.actuator)
    implementation(rootProject.libs.spring.boot.starter.jdbc)

    testImplementation(rootProject.libs.spring.boot.starter.actuatorAutoconfigure)
}
