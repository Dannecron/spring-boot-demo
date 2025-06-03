package com.github.dannecron.demo.edgecontracts.api

import com.github.dannecron.demo.edgecontracts.api.exceptions.NotFoundException
import com.github.dannecron.demo.edgecontracts.api.response.GetCustomerResponse
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.util.UUID

@RequestMapping(value = ["/api/customer"], produces = [MediaType.APPLICATION_JSON_VALUE])
interface CustomerApi {

    @GetMapping("/{guid}")
    @Throws(NotFoundException::class)
    fun getCustomer(@PathVariable guid: UUID): ResponseEntity<GetCustomerResponse>
}
