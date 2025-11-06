dependencies {
    implementation(rootProject.libs.io.ktor.ktor.client.cio)
    implementation(rootProject.libs.io.ktor.ktor.client.core)
    implementation(rootProject.libs.org.springframework.spring.context)

    testImplementation(rootProject.libs.io.ktor.ktor.client.mock)
}
