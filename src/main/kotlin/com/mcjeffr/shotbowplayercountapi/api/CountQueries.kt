package com.mcjeffr.shotbowplayercountapi.api

import com.mcjeffr.shotbowplayercountapi.configuration.CustomConfigurationProperties
import com.mcjeffr.shotbowplayercountapi.errors.InvalidParameterError
import com.mcjeffr.shotbowplayercountapi.models.AggregationType
import com.mcjeffr.shotbowplayercountapi.models.FlatCount
import com.mcjeffr.shotbowplayercountapi.services.CountService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.math.abs

@Component
class CountQueries @Autowired constructor(
        private val config: CustomConfigurationProperties,
        private val countService: CountService
) : GraphQLQueryResolver {

    private final val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    init {
        sdf.timeZone = TimeZone.getTimeZone("UTC")
    }

    fun getLatest(): FlatCount {
        return FlatCount.fromCount(countService.getLatest())
    }

    fun getInterval(from: LocalDateTime, to: LocalDateTime,
                    type: AggregationType = AggregationType.AVG, points: Int = 100): List<FlatCount> {
        if (from.isAfter(to)) {
            throw InvalidParameterError("Parameter 'from' cannot be after 'to'",
                    mapOf("from" to sdf.format(from), "to" to sdf.format(to)))
        }

        val now = LocalDateTime.now()
        val duration = abs(Duration.between(now, now.minusDays(config.maxIntervalDays.toLong())).toSeconds())
        val diff = to.toEpochSecond(ZoneOffset.UTC) - from.toEpochSecond(ZoneOffset.UTC)
        if ((diff) >= duration) {
            throw InvalidParameterError("Interval between 'from' and 'to' is too big. " +
                    "Maximum size is ${config.maxIntervalDays} days.",
                    mapOf("from" to sdf.format(from), "to" to sdf.format(to)))
        }

        if (points > config.maxPoints) {
            throw InvalidParameterError("Parameter 'points' is too big. " +
                    "Maximum size is ${config.maxPoints}.",
                    mapOf("from" to sdf.format(from), "to" to sdf.format(to)))
        }

        return countService.getInterval(from, to, points, type).map { FlatCount.fromCount(it) }
    }

}
