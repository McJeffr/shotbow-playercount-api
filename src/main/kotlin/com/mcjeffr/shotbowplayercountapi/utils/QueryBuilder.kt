package com.mcjeffr.shotbowplayercountapi.utils

import com.mcjeffr.shotbowplayercountapi.models.AggregationType
import java.time.Instant
import java.time.temporal.ChronoUnit

class QueryBuilder {
    private var bucket: String = "shotbow-playercount"
    private val games: MutableList<String> = mutableListOf()
    private var start: Instant = Instant.now().minus(1, ChronoUnit.HOURS)
    private var stop: Instant = Instant.now()
    private var interval: String = "1m"
    private var type: AggregationType = AggregationType.AVG

    fun bucket(bucket: String) = apply { this.bucket = bucket }
    fun game(game: String) = apply { games.add(game) }
    fun start(start: Instant) = apply { this.start = start }
    fun stop(stop: Instant) = apply { this.stop = stop }
    fun interval(interval: String) = apply { this.interval = interval }
    fun type(type: AggregationType) = apply { this.type = type }

    fun build(): String {
        val games = this.games.joinToString(separator = " or ") { game ->
            "r[\"_field\"] == \"${game}\""
        }
        return """
            from(bucket: "$bucket")
              |> range(start: ${start}, stop: ${stop})
              |> filter(fn: (r) => r["_measurement"] == "playercount")
              |> filter(fn: (r) => $games)
              |> aggregateWindow(every: ${interval}, fn: ${type.fnName}, createEmpty: false)
              |> yield(name: "playercounts")
        """.trimIndent()
    }
}
