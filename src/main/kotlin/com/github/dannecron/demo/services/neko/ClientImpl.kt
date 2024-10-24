package com.github.dannecron.demo.services.neko

import com.github.dannecron.demo.services.neko.dto.CategoryFormat
import com.github.dannecron.demo.services.neko.dto.ImagesResponse
import com.github.dannecron.demo.services.neko.exceptions.RequestException
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class ClientImpl(
    engine: HttpClientEngine,
    private val baseUrl: String,
): Client {
    private val httpClient = HttpClient(engine)

    override fun getCategories() = runBlocking {
        httpClient.get(urlString = baseUrl) {
            url {
                path("/api/v2/endpoints")
            }
        }
            .takeIf { it.status.value in 200..209 }
            ?.let {
                response -> Json.decodeFromString<Map<String, CategoryFormat>>(response.bodyAsText()).keys
            }
            ?: throw RequestException("get categories error")
    }

    override fun getImages(category: String, amount: Int) = runBlocking {
        httpClient.get(urlString = baseUrl) {
            url {
                path("/api/v2/$category")
                parameters.append("amount", amount.toString())
            }
        }
            .takeIf { it.status.value in 200..209 }
            ?.let {
                response ->  Json.decodeFromString<ImagesResponse>(response.bodyAsText())
            }
            ?: throw RequestException("get images error")
    }
}
