package com.github.dannecron.demo.services.database.customer

import com.github.dannecron.demo.db.entity.Customer
import com.github.dannecron.demo.models.CustomerExtended
import com.github.dannecron.demo.services.database.exceptions.CityNotFoundException
import java.util.*

interface CustomerService {
    fun findByGuid(guid: UUID): CustomerExtended?

    @Throws(CityNotFoundException::class)
    fun create(name: String, cityGuid: UUID?): Customer
}
