package com.example.demo.services.database.customer

import com.example.demo.models.Customer
import com.example.demo.models.CustomerExtended
import com.example.demo.services.database.exceptions.CityNotFoundException
import java.util.*

interface CustomerService {
    fun findByGuid(guid: UUID): CustomerExtended?

    @Throws(CityNotFoundException::class)
    fun create(name: String, cityGuid: UUID?): Customer
}
