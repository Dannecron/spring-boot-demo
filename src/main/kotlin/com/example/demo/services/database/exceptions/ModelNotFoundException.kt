package com.example.demo.services.database.exceptions

open class ModelNotFoundException(entityName: String): RuntimeException("$entityName not found")
