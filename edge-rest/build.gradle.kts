dependencies {
    implementation(project(":edge-contracts"))
    implementation(project(":core"))

    implementation(rootProject.libs.springBoot.starter.web)
    implementation(rootProject.libs.springData.commons)
}
