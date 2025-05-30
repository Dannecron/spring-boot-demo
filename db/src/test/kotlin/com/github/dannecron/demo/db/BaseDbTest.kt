package com.github.dannecron.demo.db

import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Testcontainers

@ActiveProfiles("db")
@DataJdbcTest
@Testcontainers(disabledWithoutDocker = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJdbcRepositories
class BaseDbTest
