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

/**
 * This class implements the main queries relating to player counts that the API offers.
 *
 * @author McJeffr
 */
@Component
class CountQueries @Autowired constructor(
        private val config: CustomConfigurationProperties,
        private val countService: CountService
) : GraphQLQueryResolver {

    private final val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    init {
        sdf.timeZone = TimeZone.getTimeZone("UTC")
    }

    /**
     * Gets the most recent player count that was recorded.
     *
     * @return A flat count object.
     */
    fun getLatest(): FlatCount {
        return FlatCount.fromCount(countService.getLatest())
    }

    /**
     * Gets an interval of player count objects.
     *
     * @param from The timestamp (in seconds) to start from.
     * @param to The timestamp (in seconds) to go to.
     * @param type The aggregation type that determines how data is aggregated.
     * @param points The amount of data points to aggregate the data into.
     * @return A list of flat count objects.
     */
    fun getInterval(from: LocalDateTime, to: LocalDateTime,
                    type: AggregationType = AggregationType.AVG, points: Int = 100): List<FlatCount> {
        /* Verify that the from timestamp isn't after the to timestamp */
        if (from.isAfter(to)) {
            throw InvalidParameterError("Parameter 'from' cannot be after 'to'",
                    mapOf("from" to from, "to" to to, "points" to points, "type" to type))
        }

        /* Validate that the duration (time between from and to) isn't too big */
        val now = LocalDateTime.now()
        val duration = abs(Duration.between(now, now.minusDays(config.maxIntervalDays.toLong())).toSeconds())
        val diff = to.toEpochSecond(ZoneOffset.UTC) - from.toEpochSecond(ZoneOffset.UTC)
        if ((diff) >= duration) {
            throw InvalidParameterError("Interval between 'from' and 'to' is too big. " +
                    "Maximum size is ${config.maxIntervalDays} days.",
                    mapOf("from" to from, "to" to to, "points" to points, "type" to type))
        }

        /* Validate that the amount of data points requested isn't too big */
        if (points > config.maxPoints) {
            throw InvalidParameterError("Parameter 'points' is too big. " +
                    "Maximum size is ${config.maxPoints}.",
                    mapOf("from" to from, "to" to to, "points" to points, "type" to type))
        }

        /* Fetch the interval from the count service, convert it to flat counts, and return it */
        return countService.getInterval(from, to, points, type).map { FlatCount.fromCount(it) }
    }

}
