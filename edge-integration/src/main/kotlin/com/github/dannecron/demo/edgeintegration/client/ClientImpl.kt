package com.github.dannecron.demo.edgeintegration.client

import com.github.dannecron.demo.edgeintegration.client.dto.CategoryFormat
import com.github.dannecron.demo.edgeintegration.client.dto.ImagesResponse
import com.github.dannecron.demo.edgeintegration.client.exceptions.RequestException
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.path
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service

@Service
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
