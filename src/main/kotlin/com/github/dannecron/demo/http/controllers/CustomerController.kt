package com.github.dannecron.demo.http.controllers

import com.github.dannecron.demo.core.services.customer.CustomerService
import com.github.dannecron.demo.http.exceptions.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping(value = ["/api/customer"], produces = [MediaType.APPLICATION_JSON_VALUE])
class CustomerController(
    @Autowired
    private val customerService: CustomerService,
) {
    @GetMapping("/{guid}")
    @Throws(NotFoundException::class)
    fun getCustomer(
        @PathVariable guid: UUID,
    ): ResponseEntity<Any> {
        val customer = customerService.findByGuid(guid) ?: throw NotFoundException()

        return ResponseEntity(customer, HttpStatus.OK)
    }
}
