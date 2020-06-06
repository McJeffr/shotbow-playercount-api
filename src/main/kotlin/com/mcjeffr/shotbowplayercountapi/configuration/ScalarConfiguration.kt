package com.mcjeffr.shotbowplayercountapi.configuration

import com.mcjeffr.shotbowplayercountapi.models.AggregationType
import graphql.language.IntValue
import graphql.language.StringValue
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
                    throw CoercingSerializeException("Provided object to serialize is not of type 'Date'")
                }
                return o.time / 1000
            }

            override fun parseValue(o: Any): Date {
                if (o !is Long) {
                    throw CoercingParseValueException("Provided object to parse is not of GraphQL type 'Long'")
                }
                return Date(o * 1000)
            }

            override fun parseLiteral(o: Any): Date {
                if (o !is IntValue) {
                    throw CoercingParseValueException("Provided object to parse is not of GraphQL type 'Int'")
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
