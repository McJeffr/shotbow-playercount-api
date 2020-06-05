package com.mcjeffr.shotbowplayercountapi.configuration

import graphql.language.IntValue
import graphql.schema.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class ScalarConfiguration {

    @Bean
    fun timestampScalar(): GraphQLScalarType {
        val coercing: Coercing<Date, Long> = object : Coercing<Date, Long> {
            override fun serialize(o: Any): Long {
                if (o !is Date) {
                    throw CoercingSerializeException("Provided object to serialize is not of type Date")
                }
                return o.time / 1000
            }

            override fun parseValue(o: Any): Date {
                if (o !is Long) {
                    throw CoercingParseValueException("Provided object to serialize is not of GraphQL type 'Long'")
                }
                return Date(o * 1000)
            }

            override fun parseLiteral(o: Any): Date {
                if (o !is IntValue) {
                    throw CoercingParseValueException("Provided object to serialize is not of GraphQL type 'Int'")
                }
                return Date(o.value.toLong() * 1000)
            }
        }

        return GraphQLScalarType.newScalar()
                .name("Timestamp")
                .coercing(coercing)
                .description("Unix Timestamp scalar type")
                .build()
    }

}