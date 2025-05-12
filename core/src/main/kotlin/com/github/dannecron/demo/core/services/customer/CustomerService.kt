package com.github.dannecron.demo.core.services.customer

import com.github.dannecron.demo.core.dto.Customer
import com.github.dannecron.demo.core.dto.view.CustomerExtended
import com.github.dannecron.demo.core.exceptions.CityNotFoundException
import java.util.UUID

interface CustomerService {
    fun findByGuid(guid: UUID): CustomerExtended?

    @Throws(CityNotFoundException::class)
    fun create(name: String, cityGuid: UUID?): Customer
}
