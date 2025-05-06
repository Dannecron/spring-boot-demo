package com.github.dannecron.demo.db.repository

import com.github.dannecron.demo.db.entity.CustomerEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CustomerRepository: CrudRepository<CustomerEntity, Long> {
    fun findByGuid(guid: UUID): CustomerEntity?
}
