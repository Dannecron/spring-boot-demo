package com.github.dannecron.demo

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.library.Architectures
import org.junit.jupiter.api.Test

class ArchTest {

    companion object {
        // high-level layers
        private const val LAYER_EDGE_CONSUMING = "edge-consuming"
        private const val LAYER_EDGE_REST = "edge-rest"
        // core
        private const val LAYER_CORE = "core"
        // low-level layers
        private const val LAYER_DB = "db"
        private const val LAYER_EDGE_INTEGRATIONS = "edge-integrations"
        private const val LAYER_EDGE_PRODUCING = "edge-producing"
        // edge-contracts
        private const val LAYER_EDGE_CONTRACTS = "edge-contracts"
    }

    private val config: List<Triple<String, String, List<String>>> = listOf(
        Triple(LAYER_EDGE_CONSUMING, "edgeconsuming", emptyList()),
        Triple(LAYER_EDGE_REST, "edgerest", emptyList()),
        Triple(LAYER_CORE, "core", listOf(LAYER_EDGE_CONSUMING, LAYER_EDGE_REST)),
        Triple(LAYER_DB, "db", listOf(LAYER_CORE)),
        Triple(LAYER_EDGE_INTEGRATIONS, "edgeintegration", listOf(LAYER_CORE)),
        Triple(LAYER_EDGE_PRODUCING, "edgeproducing", listOf(LAYER_CORE)),
        Triple(
            LAYER_EDGE_CONTRACTS,
            "edgecontracts",
            listOf(LAYER_EDGE_INTEGRATIONS, LAYER_EDGE_PRODUCING, LAYER_EDGE_CONSUMING, LAYER_EDGE_REST),
        ),

    )

    @Test
    fun `check layers`() {
        val mainSrcPackage = this::class.java.packageName
        val allProjectClasses = ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(mainSrcPackage)

        Architectures.layeredArchitecture()
            .consideringAllDependencies().let { arch ->
                config.fold(arch) { archLocal, conf ->
                    val (layerName, layerNamespace, acceptedByLayers) = conf
                    archLocal.layer(layerName).definedBy("$mainSrcPackage.$layerNamespace..").let {
                        if (acceptedByLayers.isNotEmpty())
                            it.whereLayer(layerName).mayOnlyBeAccessedByLayers(*acceptedByLayers.toTypedArray())
                        else
                            it
                    }
                }
            }.also {
                it.check(allProjectClasses)
            }
    }
}
