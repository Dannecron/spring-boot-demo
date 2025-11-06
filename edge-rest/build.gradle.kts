dependencies {
    implementation(project(":edge-contracts"))
    implementation(project(":core"))

    implementation(rootProject.libs.org.springframework.boot.spring.boot.starter.web)
    implementation(rootProject.libs.org.springframework.data.spring.data.commons)
}
