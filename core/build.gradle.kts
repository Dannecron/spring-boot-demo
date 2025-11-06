dependencies {
    implementation(project(":db"))
    implementation(project(":edge-producing"))
    implementation(project(":edge-integration"))

    implementation(rootProject.libs.org.springframework.boot.spring.boot.starter.actuator)
    implementation(rootProject.libs.org.springframework.data.spring.data.commons)

    testImplementation(rootProject.libs.org.springframework.boot.spring.boot.starter.actuator.autoconfigure)
}
