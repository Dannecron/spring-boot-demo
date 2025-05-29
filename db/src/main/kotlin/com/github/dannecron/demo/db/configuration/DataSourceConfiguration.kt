package com.github.dannecron.demo.db.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration

@Configuration
class DataSourceConfiguration(private val converters: List<Converter<*, *>>) : AbstractJdbcConfiguration() {
    override fun jdbcCustomConversions(): JdbcCustomConversions = JdbcCustomConversions(converters)
}
