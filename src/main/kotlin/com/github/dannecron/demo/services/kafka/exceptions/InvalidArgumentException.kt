package com.github.dannecron.demo.services.kafka.exceptions

class InvalidArgumentException(argName: String): RuntimeException("invalid argument $argName")
