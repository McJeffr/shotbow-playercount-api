package com.mcjeffr.shotbowplayercountapi.services

import com.influxdb.query.FluxTable
import com.mcjeffr.shotbowplayercountapi.configuration.CustomConfigurationProperties
import com.mcjeffr.shotbowplayercountapi.configuration.InfluxConfigurationProperties
import com.mcjeffr.shotbowplayercountapi.models.AggregationType
import com.mcjeffr.shotbowplayercountapi.models.Count
import com.mcjeffr.shotbowplayercountapi.models.CountComponent
import com.mcjeffr.shotbowplayercountapi.utils.QueryBuilder
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * This class contains the count service. This service is responsible for dealing with retrieving
 * and processing count objects.
 *
 * @author McJeffr
 */
@Service
class CountService(
    private val gameConfig: CustomConfigurationProperties,
    private val influxConfig: InfluxConfigurationProperties,
    private val influxService: InfluxService
) {

    private val latestQuery = """
        from(bucket: "${influxConfig.bucket}")
          |> range(start: -1m)
          |> filter(fn: (r) => r["_measurement"] == "playercount")
          |> last()
          |> yield(name: "playercount")
    """

    /**
     * Gets the most recent count object that was inserted into the database.
     *
     * @return The most recent count object that was inserted into the database.
     */
    fun getLatest(): Count? {
        val tables = influxService.query(latestQuery)
        if (tables.isEmpty()) {
            return null
        }

        val counts = mergeTables(tables)
        return if (counts.isNotEmpty()) {
            counts[0]
        } else {
            null
        }
    }

    /**
     * Gets a list of count objects that span the provided interval. No validation is taking place
     * to verify that the provided interval is correct, not if the parameters follow the constraints
     * that have been set in place.
     *
     * @param from The timestamp to start at.
     * @param to The timestamp to end at.
     * @param type The aggregation type. Defaults to AVG.
     * @param interval The size of the window in the query.
     * @return A list of count components based on the provided inputs.
     */
    fun getInterval(
        from: Instant = Instant.now().minus(1, ChronoUnit.HOURS),
        to: Instant = Instant.now(),
        type: AggregationType = AggregationType.AVG,
        interval: String = "1m"
    ): List<Count> {
        val queryBuilder = QueryBuilder()
            .bucket(influxConfig.bucket)
            .type(type)
            .start(from)
            .stop(to)
            .interval(interval)
        gameConfig.gamemodes.forEach { gamemode ->
            queryBuilder.game(gamemode)
        }
        val query = queryBuilder.build()
        val tables = influxService.query(query)
        if (tables.isEmpty()) {
            return listOf()
        }

        return mergeTables(tables)
    }

    private fun mergeTables(tables: List<FluxTable>): List<Count> {
        if (tables.isEmpty()) {
            return listOf()
        }

        val counts: MutableList<Count> = mutableListOf()
        val amountOfPoints = tables[0].records.size
        for (i in 0 until amountOfPoints) {
            val timestamp = tables[0].records[i].time!!
            val components = tables.map { table ->
                val value = table.records[i].getValueByKey("_value")
                val parsedValue: Int =
                    if (value is Double) value.toInt() else if (value is Long) value.toInt() else -1
                CountComponent(
                    name = table.records[i].field!!,
                    value = parsedValue
                )
            }
            counts.add(
                Count(
                    timestamp = timestamp,
                    components = components.toMutableList()
                )
            )
        }

        return counts.toList()
    }

}
