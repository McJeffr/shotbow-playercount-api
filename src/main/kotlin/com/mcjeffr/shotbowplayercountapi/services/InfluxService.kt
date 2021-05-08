package com.mcjeffr.shotbowplayercountapi.services

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.InfluxDBClientFactory
import com.influxdb.client.QueryApi
import com.influxdb.client.WriteApi
import com.influxdb.client.write.Point
import com.influxdb.query.FluxTable
import com.mcjeffr.shotbowplayercountapi.configuration.InfluxConfigurationProperties
import org.springframework.stereotype.Service


@Service
class InfluxService(influxConfig: InfluxConfigurationProperties) {

    private val influxDbClient: InfluxDBClient = InfluxDBClientFactory.create(
        influxConfig.url,
        influxConfig.token.toCharArray(),
        influxConfig.org,
        influxConfig.bucket
    )

    fun writePoint(point: Point) {
        val writeApi: WriteApi = influxDbClient.writeApi
        writeApi.use {
            writeApi.writePoint(point)
        }
    }

    fun query(query: String): List<FluxTable> {
        val queryApi: QueryApi = influxDbClient.queryApi
        return queryApi.query(query)
    }

}
