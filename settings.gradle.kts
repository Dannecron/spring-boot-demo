plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "demo"
include("db")
include("core")
include("edge-consuming")
include("edge-producing")
include("edge-contracts")
