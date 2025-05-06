package com.github.dannecron.demo.core.exceptions

open class ModelNotFoundException(entityName: String): RuntimeException("$entityName not found")
