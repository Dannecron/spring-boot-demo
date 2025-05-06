group = "com.github.dannecron.demo"
version = "single-version"

dependencies {
    implementation(project(":db"))

    implementation(rootProject.libs.spring.boot.starter.jdbc)
    implementation(rootProject.libs.spring.boot.starter.validation)
    implementation(rootProject.libs.json.schema.validator)
}
