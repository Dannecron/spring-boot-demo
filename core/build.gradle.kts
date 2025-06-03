dependencies {
    implementation(project(":db"))
    implementation(project(":edge-producing"))
    implementation(project(":edge-integration"))

    implementation(rootProject.libs.springBoot.starter.actuator)
    implementation(rootProject.libs.springData.commons)

    testImplementation(rootProject.libs.springBoot.starter.actuatorAutoconfigure)
}
