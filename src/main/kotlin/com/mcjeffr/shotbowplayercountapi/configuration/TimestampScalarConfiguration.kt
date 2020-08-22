package com.mcjeffr.shotbowplayercountapi.configuration

import graphql.language.IntValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * This class contains the configuration for the custom Timestamp scalar. This scalar is used to
 * represent a timestamp in UNIX time.
 *
 * @author McJeffr
 */
@Configuration
class TimestampScalarConfiguration {

    @Bean
    fun timestampScalar(): GraphQLScalarType {
        val coercing: Coercing<LocalDateTime, Long> = object : Coercing<LocalDateTime, Long> {
            override fun serialize(o: Any): Long {
                if (o !is LocalDateTime) {
                    throw CoercingSerializeException("Provided object to serialize is not of type 'Date'")
                }
                return o.toEpochSecond(ZoneOffset.UTC)
            }

            override fun parseValue(o: Any): LocalDateTime {
                if (o !is Long) {
                    throw CoercingParseValueException("Provided object to parse is not of GraphQL type 'Long'")
                }
                return LocalDateTime.ofInstant(Instant.ofEpochSecond(o), ZoneId.of("UTC"))
            }

            override fun parseLiteral(o: Any): LocalDateTime {
                if (o !is IntValue) {
                    throw CoercingParseValueException("Provided object to parse is not of GraphQL type 'Int'")
                }
                return LocalDateTime.ofInstant(Instant.ofEpochSecond(o.value.toLong()),
                        ZoneId.of("UTC"))
            }
        }

        return GraphQLScalarType.newScalar()
                .name("Timestamp")
                .coercing(coercing)
                .description("Unix Timestamp scalar type")
                .build()
    }

}
