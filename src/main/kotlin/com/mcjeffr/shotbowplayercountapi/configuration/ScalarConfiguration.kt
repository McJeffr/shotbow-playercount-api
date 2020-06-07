package com.mcjeffr.shotbowplayercountapi.configuration

import com.mcjeffr.shotbowplayercountapi.models.AggregationType
import graphql.language.IntValue
import graphql.language.StringValue
import graphql.schema.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

@Configuration
class ScalarConfiguration {

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

    @Bean
    fun aggregateScalar(): GraphQLScalarType {
        val coercing: Coercing<AggregationType, String> = object : Coercing<AggregationType, String> {
            override fun serialize(o: Any?): String {
                if (o !is AggregationType) {
                    throw CoercingSerializeException("Provided object to serialize is not of type 'AggregationType'")
                }
                return o.name
            }

            override fun parseValue(o: Any?): AggregationType {
                if (o !is String) {
                    throw CoercingParseValueException("Provided object to parse is not of GraphQL type 'String'")
                }
                return AggregationType.valueOf(o)
            }

            override fun parseLiteral(o: Any?): AggregationType {
                if (o !is StringValue) {
                    throw CoercingParseValueException("Provided object to parse is not of GraphQL type 'String'")
                }
                return AggregationType.valueOf(o.value)
            }
        }

        return GraphQLScalarType.newScalar()
                .name("Aggregation")
                .coercing(coercing)
                .description("Aggregation scalar type. Valid options are: AVG, MIN and MAX.")
                .build()
    }

}
