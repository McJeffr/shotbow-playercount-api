package com.mcjeffr.shotbowplayercountapi.api

import com.mcjeffr.shotbowplayercountapi.configuration.CustomConfigurationProperties
import com.mcjeffr.shotbowplayercountapi.errors.InvalidParameterError
import com.mcjeffr.shotbowplayercountapi.models.AggregationType
import com.mcjeffr.shotbowplayercountapi.models.FlatCount
import com.mcjeffr.shotbowplayercountapi.services.CountService
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
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

    companion object {
        val DURATION_REGEX: Regex = Regex("\\d*(y|mo|w|d|h|m|s)")
    }

    /**
     * Gets the most recent player count that was recorded.
     *
     * @return A flat count object.
     */
    fun getLatest(): FlatCount? {
        val latestCount = countService.getLatest()
        return if (latestCount != null) {
            FlatCount.fromCount(latestCount)
        } else {
            null
        }
    }

    /**
     * Gets an interval of player count objects.
     *
     * @param from The timestamp (in seconds) to start from.
     * @param to The timestamp (in seconds) to go to.
     * @param type The aggregation type that determines how data is aggregated.
     * @param interval The size of the window in the data.
     * @return A list of flat count objects.
     */
    fun getInterval(
        from: Instant = Instant.now().minus(1, ChronoUnit.HOURS),
        to: Instant = Instant.now(),
        type: AggregationType = AggregationType.AVG,
        interval: String = "1m"
    ): List<FlatCount> {
        /* Verify that the from timestamp isn't after the to timestamp */
        if (from.isAfter(to)) {
            throw InvalidParameterError(
                "Parameter 'from' cannot be after 'to'",
                mapOf("from" to from, "to" to to, "interval" to interval, "type" to type)
            )
        }

        /* Validate that the duration (time between from and to) isn't too big */
        if (config.maxIntervalDays > 0) {
            val now = LocalDateTime.now()
            val duration = abs(
                Duration.between(now, now.minusDays(config.maxIntervalDays.toLong())).toSeconds()
            )
            val diff = to.epochSecond - from.epochSecond
            if ((diff) >= duration) {
                throw InvalidParameterError(
                    "Interval between 'from' and 'to' is too big. " +
                            "Maximum size is ${config.maxIntervalDays} days.",
                    mapOf("from" to from, "to" to to, "interval" to interval, "type" to type)
                )
            }
        }

        /* Validate the interval parameter */
        if (!interval.matches(DURATION_REGEX)) {
            throw InvalidParameterError(
                "Parameter 'interval' does not match allowed duration syntax. " +
                        "Duration syntax is ${DURATION_REGEX.pattern}.",
                mapOf("from" to from, "to" to to, "interval" to interval, "type" to type)
            )
        }

        /* Fetch the interval from the count service, convert it to flat counts, and return it */
        return countService.getInterval(from, to, type, interval).map { FlatCount.fromCount(it) }
    }

}
