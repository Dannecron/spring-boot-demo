package com.github.dannecron.demo.edgerest.controllers

import com.github.dannecron.demo.core.dto.Product
import com.github.dannecron.demo.core.exceptions.AlreadyDeletedException
import com.github.dannecron.demo.core.exceptions.InvalidDataException
import com.github.dannecron.demo.core.exceptions.ProductNotFoundException
import com.github.dannecron.demo.core.services.product.ProductService
import com.github.dannecron.demo.edgecontracts.api.ProductApi
import com.github.dannecron.demo.edgecontracts.api.exceptions.NotFoundException
import com.github.dannecron.demo.edgecontracts.api.exceptions.UnprocessableException
import com.github.dannecron.demo.edgecontracts.api.model.ProductApiModel
import com.github.dannecron.demo.edgecontracts.api.request.CreateProductRequest
import com.github.dannecron.demo.edgecontracts.api.response.common.BaseResponse
import com.github.dannecron.demo.edgecontracts.api.response.common.makeOkResponse
import com.github.dannecron.demo.edgecontracts.api.response.page.PageResponse
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class ProductController(
    private val productService: ProductService,
) : ProductApi {
    override fun getProducts(pageable: Pageable): ResponseEntity<PageResponse<ProductApiModel>> {
        val products = productService.findAll(pageable)

        return ResponseEntity(
            PageResponse(products.map { it.toApiModel() }),
            HttpStatus.OK,
        )
    }

    @Throws(NotFoundException::class)
    override fun getProduct(guid: UUID): ResponseEntity<ProductApiModel> {
        val product = productService.findByGuid(guid = guid) ?: throw NotFoundException(null)

        return ResponseEntity(product.toApiModel(), HttpStatus.OK)
    }

    override fun createProduct(product: CreateProductRequest): ResponseEntity<ProductApiModel> {
        val saved = productService.create(
            product.name,
            product.price,
            product.description,
        )

        return ResponseEntity(saved.toApiModel(), HttpStatus.CREATED)
    }

    @Throws(NotFoundException::class, UnprocessableException::class)
    override fun deleteProduct(guid: UUID): ResponseEntity<BaseResponse> {
        try {
            productService.delete(guid)
            return ResponseEntity(makeOkResponse(), HttpStatus.OK)
        } catch (ex: ProductNotFoundException) {
            throw NotFoundException(ex)
        } catch (ex: AlreadyDeletedException) {
            throw UnprocessableException("product already deleted", ex)
        }
    }

    @Throws(NotFoundException::class)
    override fun sendProduct(guid: UUID, topic: String?): ResponseEntity<BaseResponse> {
        try {
            productService.send(guid, topic)

            return ResponseEntity(makeOkResponse(), HttpStatus.OK)
        } catch (_: InvalidDataException) {
            throw UnprocessableException("cannot sync product")
        }
    }

    private fun Product.toApiModel() = ProductApiModel(
        id = id,
        guid = guid,
        name = name,
        description = description,
        price = price,
        createdAt = createdAt,
        updatedAt = updatedAt,
        deletedAt = deletedAt,
        priceDouble = getPriceDouble(),
        isDeleted = isDeleted(),
    )
}
