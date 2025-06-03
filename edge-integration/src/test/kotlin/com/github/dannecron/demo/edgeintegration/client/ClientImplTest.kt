package com.github.dannecron.demo.edgeintegration.client

import com.github.dannecron.demo.edgeintegration.client.exceptions.RequestException
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ClientImplTest {
    @Test
    fun getCategories_success() {
        val mockEngine = MockEngine { req ->
            assertEquals("localhost", req.url.host)
            assertEquals("/api/v2/endpoints", req.url.encodedPath)

            respond(
                content = ByteReadChannel(
                    """
                        {"neko": {"format": "png"},"wink": {"format": "gif"}}
                    """.trimIndent()
                ),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }

        val client = ClientImpl(engine = mockEngine, baseUrl = "https://localhost")
        client.getCategories().let {
            assertContains(it.toList(), "neko")
            assertContains(it.toList(), "wink")
        }
    }

    @Test
    fun getCategories_fail() {
        val mockEngine = MockEngine { req ->
            assertEquals("localhost", req.url.host)
            assertEquals("/api/v2/endpoints", req.url.encodedPath)

            respond(
                content = ByteReadChannel(""),
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, "text/plain"),
            )
        }

        val client = ClientImpl(engine = mockEngine, baseUrl = "https://localhost")
        assertThrows<RequestException> {
            client.getCategories()
        }
    }

    @Test
    fun getImages_success_gifs() {
        val category = "hug"
        val amount = 2

        val mockEngine = MockEngine { req ->
            assertEquals("localhost", req.url.host)
            assertEquals("/api/v2/$category", req.url.encodedPath)
            assertTrue {
                req.url.parameters.contains("amount", amount.toString())
            }

            respond(
                content = ByteReadChannel(
                    """{"results":[
                        {"anime_name":"Sword Art Online", 
                            "url":"https://nekos.best/api/v2/hug/c6a7d384-dc40-11ed-afa1-0242ac120002.gif"
                        },
                        {"anime_name":"Hibike! Euphonium",
                            "url":"https://nekos.best/api/v2/hug/ca26cfba-dc40-11ed-afa1-0242ac120002.gif"
                        }
                    ]}""".trimIndent(),
                ),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }

        val client = ClientImpl(engine = mockEngine, baseUrl = "https://localhost")
        client.getImages(category = category, amount = amount).results.map {
            assertNull(it.sourceUrl)
            assertNull(it.artistName)
            assertNull(it.artistHref)
            assertNotNull(it.animeName)
            assertContains(it.url, "https://nekos.best/api/v2/hug")
        }
    }

    @Test
    fun getImages_success_jpegs() {
        val category = "neko"
        val amount = 1

        val mockEngine = MockEngine { req ->
            assertEquals("localhost", req.url.host)
            assertEquals("/api/v2/$category", req.url.encodedPath)
            assertTrue {
                req.url.parameters.contains("amount", amount.toString())
            }

            respond(
                content = ByteReadChannel(
                    """{"results":[
                       {
                          "artist_href":"https://www.pixiv.net/en/users/47065875",
                          "artist_name":"かえで",
                          "source_url":"https://www.pixiv.net/en/artworks/88682108",
                          "url":"https://nekos.best/api/v2/neko/bbffa4e8-dc40-11ed-afa1-0242ac120002.png"
                       }
                    ]}""".trimIndent(),
                ),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }

        val client = ClientImpl(engine = mockEngine, baseUrl = "https://localhost")
        client.getImages(category = category, amount = amount).results.map {
            assertNotNull(it.sourceUrl)
            assertNotNull(it.artistName)
            assertNotNull(it.artistHref)
            assertNull(it.animeName)
            assertContains(it.url, "https://nekos.best/api/v2/neko")
        }
    }

    @Test
    fun getImages_fail() {
        val category = "hug"
        val amount = 2

        val mockEngine = MockEngine { req ->
            assertEquals("localhost", req.url.host)
            assertEquals("/api/v2/$category", req.url.encodedPath)
            assertTrue {
                req.url.parameters.contains("amount", amount.toString())
            }

            respond(
                content = ByteReadChannel(""),
                status = HttpStatusCode.InternalServerError,
                headers = headersOf(HttpHeaders.ContentType, "plain/text"),
            )
        }

        val client = ClientImpl(engine = mockEngine, baseUrl = "https://localhost")
        assertThrows<RequestException> {
            client.getImages(category = category, amount = amount)
        }
    }
}
