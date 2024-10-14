package com.example.demo.providers

import com.example.demo.models.Customer
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CustomerRepository: CrudRepository<Customer, Long> {
    fun findByGuid(guid: UUID): Customer?
}
