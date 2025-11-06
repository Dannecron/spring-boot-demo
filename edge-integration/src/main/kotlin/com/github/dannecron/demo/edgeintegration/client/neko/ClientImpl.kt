package com.github.dannecron.demo.edgeintegration.client.neko

import com.github.dannecron.demo.edgeintegration.client.neko.dto.CategoryFormat
import com.github.dannecron.demo.edgeintegration.client.neko.dto.ImagesResponse
import com.github.dannecron.demo.edgeintegration.client.neko.exceptions.RequestException
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.path
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class ClientImpl(
    engine: HttpClientEngine,
    private val baseUrl: String,
): Client {
    private val httpClient = HttpClient(engine) {
        defaultRequest {
            url(baseUrl)
        }
    }

    override fun getCategories() = runBlocking {
        httpClient.get {
            url {
                path("/api/v2/endpoints")
            }
        }
            .let { response ->
                val responseBody = response.bodyAsText()
                if (response.status.value in 200..209) {
                    Json.decodeFromString<Map<String, CategoryFormat>>(responseBody).keys
                } else {
                    throw RequestException(
                        "get categories error. Status: ${response.status.value}, response: $responseBody"
                    )
                }
            }
    }

    override fun getImages(category: String, amount: Int) = runBlocking {
        httpClient.get {
            url {
                path("/api/v2/$category")
                parameters.append("amount", amount.toString())
            }
        }
            .let { response ->
                val responseBody = response.bodyAsText()
                if (response.status.value in 200..209) {
                    Json.decodeFromString<ImagesResponse>(responseBody)
                } else {
                    throw RequestException(
                        "get images error. Status: ${response.status.value}, response: $responseBody"
                    )
                }
            }
    }
}
