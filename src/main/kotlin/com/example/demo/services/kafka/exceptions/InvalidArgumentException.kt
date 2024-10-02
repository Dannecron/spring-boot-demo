package com.example.demo.services.kafka.exceptions

class InvalidArgumentException(argName: String): RuntimeException("invalid argument $argName")