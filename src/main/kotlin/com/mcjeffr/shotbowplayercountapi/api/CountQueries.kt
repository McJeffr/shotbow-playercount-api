package com.mcjeffr.shotbowplayercountapi.api

import com.mcjeffr.shotbowplayercountapi.configuration.CustomConfigurationProperties
import com.mcjeffr.shotbowplayercountapi.errors.InvalidParameterError
import com.mcjeffr.shotbowplayercountapi.models.Count
import com.mcjeffr.shotbowplayercountapi.repositories.CountRepository
import graphql.kickstart.tools.GraphQLQueryResolver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.math.abs

@Component
class CountQueries @Autowired constructor(
        private val config: CustomConfigurationProperties,
        private val countRepository: CountRepository
) : GraphQLQueryResolver {

    private final val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    init {
        sdf.timeZone = TimeZone.getTimeZone("UTC")
    }

    fun getLatest(): Count {
        return countRepository.getLatest()
    }

    fun getInterval(from: Date, to: Date): List<Count> {
        if (from.after(to)) {
            throw InvalidParameterError("Parameter 'from' cannot be after 'to'",
                    mapOf("from" to sdf.format(from), "to" to sdf.format(to)))
        }

        val now = LocalDateTime.now()
        val duration = abs(Duration.between(now, now.minusDays(config.maxIntervalDays.toLong())).toMillis())
        val diff = to.time - from.time
        if ((diff) >= duration) {
            throw InvalidParameterError("Interval between 'from' and 'to' is too big. " +
                    "Maximum size is ${config.maxIntervalDays} days",
                    mapOf("from" to sdf.format(from), "to" to sdf.format(to)))
        }

        return countRepository.getInterval(from, to)
    }

}
