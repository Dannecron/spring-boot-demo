package com.github.dannecron.demo.core.services.validation

import com.github.dannecron.demo.core.services.validation.exceptions.ElementNotValidException
import com.github.dannecron.demo.core.services.validation.exceptions.SchemaNotFoundException
import kotlinx.serialization.json.Json
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.reflect.KClass
import kotlin.test.assertFailsWith

class SchemaValidatorImpTest {
    private val schemaValidatorImp = SchemaValidatorImp(
        schemaMap = mapOf(
            KAFKA_PRODUCT_SYNC_SCHEMA to getJsonSchema("json-schemas/kafka/product/sync.json")),
    )

    @ParameterizedTest
    @MethodSource("validateDataProvider")
    fun validate(schemaName: String, inputRawJson: String, expectedException: KClass<out Throwable>?) {
        val element = Json.parseToJsonElement(inputRawJson.trimIndent())

        if (expectedException == null) {
            schemaValidatorImp.validate(schemaName = schemaName, value = element)

            return
        }

        assertFailsWith(expectedException) {
            schemaValidatorImp.validate(schemaName = schemaName, value = element)
        }
    }

    companion object {
        private const val KAFKA_PRODUCT_SYNC_SCHEMA = "kafkaProductSync"

        @JvmStatic
        fun validateDataProvider() = listOf(
            Arguments.of(
                KAFKA_PRODUCT_SYNC_SCHEMA,
                """
                {
                  "id": 123,
                  "guid": "3a27e322-b5b6-427f-b761-a02284c1cfa4",
                  "name": "some-name",
                  "description": null,
                  "price": 12.22,
                  "createdAt": "2024-01-01T12:11:10+04:00",
                  "updatedAt": null,
                  "deletedAt": null
                }
                """,
                null,
            ),
            Arguments.of( // no id
                KAFKA_PRODUCT_SYNC_SCHEMA,
                """
                {
                    "guid": "3a27e322-b5b6-427f-b761-a02284c1cfa4",
                    "name": "some-name",
                    "description": null,
                    "price": 12.22,
                    "createdAt": "2024-01-01T12:11:10+04:00",
                    "updatedAt": null,
                    "deletedAt": null
                }
                """,
                ElementNotValidException::class,
            ),
            Arguments.of( // wrong guid
                KAFKA_PRODUCT_SYNC_SCHEMA,
                """
                {
                    "id": 213,
                    "guid": 77373,
                    "name": "some-name",
                    "description": null,
                    "price": 12.22,
                    "createdAt": "2024-01-01T12:11:10+04:00",
                    "updatedAt": null,
                    "deletedAt": null
                }
                """,
                ElementNotValidException::class,
            ),
            Arguments.of(
                "some-unknown-schema",
                "{}",
                SchemaNotFoundException::class,
            )
        )
    }

    private fun getJsonSchema(resourcePath: String) = javaClass.classLoader
        .getResourceAsStream(resourcePath)!!
        .readAllBytes()
        .toString(Charsets.UTF_8)
}
