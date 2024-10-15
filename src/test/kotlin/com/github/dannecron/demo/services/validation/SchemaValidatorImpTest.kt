package com.github.dannecron.demo.services.validation

import com.github.dannecron.demo.BaseUnitTest
import com.github.dannecron.demo.services.validation.SchemaValidator.Companion.SCHEMA_KAFKA_PRODUCT_SYNC
import com.github.dannecron.demo.services.validation.exceptions.ElementNotValidException
import com.github.dannecron.demo.services.validation.exceptions.SchemaNotFoundException
import kotlinx.serialization.json.Json
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import kotlin.reflect.KClass
import kotlin.test.assertFailsWith

@RunWith(SpringRunner::class)
@SpringBootTest
class SchemaValidatorImpTest(
    @Autowired val schemaValidatorImp: SchemaValidatorImp
): BaseUnitTest() {
    @ParameterizedTest
    @MethodSource("validateDataProvider")
    fun validate(schemaName: String, inputRawJson: String, expectedException: KClass<out Throwable>?) {
        val element = Json.parseToJsonElement(inputRawJson.trimIndent())

        if (expectedException == null) {
            schemaValidatorImp.validate(schemaName = schemaName, value = element)
            // second time should use cache
            schemaValidatorImp.validate(schemaName = schemaName, value = element)

            return
        }

        assertFailsWith(expectedException) {
            schemaValidatorImp.validate(schemaName = schemaName, value = element)
        }
    }

    companion object {
        @JvmStatic
        fun validateDataProvider() = listOf(
            Arguments.of(
                SCHEMA_KAFKA_PRODUCT_SYNC,
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
                SCHEMA_KAFKA_PRODUCT_SYNC,
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
                SCHEMA_KAFKA_PRODUCT_SYNC,
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
}
