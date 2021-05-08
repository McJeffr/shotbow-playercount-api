package com.mcjeffr.shotbowplayercountapi.configuration

import graphql.language.IntValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import graphql.schema.GraphQLScalarType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant

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
        val coercing: Coercing<Instant, Long> = object : Coercing<Instant, Long> {
            override fun serialize(o: Any): Long {
                if (o !is Instant) {
                    throw CoercingSerializeException("Provided object to serialize is not of type 'Date'")
                }
                return o.epochSecond
            }

            override fun parseValue(o: Any): Instant {
                if (o !is Long) {
                    throw CoercingParseValueException("Provided object to parse is not of GraphQL type 'Long'")
                }
                return Instant.ofEpochSecond(o)
            }

            override fun parseLiteral(o: Any): Instant {
                if (o !is IntValue) {
                    throw CoercingParseValueException("Provided object to parse is not of GraphQL type 'Int'")
                }
                return Instant.ofEpochSecond(o.value.toLong())
            }
        }

        return GraphQLScalarType.newScalar()
            .name("Timestamp")
            .coercing(coercing)
            .description("Unix Timestamp scalar type")
            .build()
    }

}
