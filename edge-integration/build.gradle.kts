dependencies {
    implementation(rootProject.libs.springFramework.context)
    implementation(rootProject.libs.ktor.client.cio)
    implementation(rootProject.libs.ktor.client.core)

    testImplementation(rootProject.libs.ktor.client.mock)
}
