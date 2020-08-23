package com.mcjeffr.shotbowplayercountapi.services

import com.mcjeffr.shotbowplayercountapi.models.AggregationType
import com.mcjeffr.shotbowplayercountapi.models.CompositeCountComponent
import com.mcjeffr.shotbowplayercountapi.models.Count
import com.mcjeffr.shotbowplayercountapi.repositories.CountRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.math.ceil

/**
 * This class contains the count service. This service is responsible for dealing with retrieving
 * and processing count objects.
 *
 * @author McJeffr
 */
@Service
class CountService(private val countRepository: CountRepository) {

    /**
     * Gets the most recent count object that was inserted into the database.
     *
     * @return The most recent count object that was inserted into the database.
     */
    fun getLatest(): Count {
        return countRepository.getLatest()
    }

    /**
     * Gets a list of count objects that span the provided interval. No validation is taking place
     * to verify that the provided interval is correct, not if the parameters follow the constraints
     * that have been set in place.
     *
     * @param from The timestamp to start at.
     * @param to The timestamp to end at.
     * @param amountOfPoints The amount of data points to return. Defaults to 100.
     * @param type The aggregation type. Defaults to AVG.
     * @return A list of count components based on the provided inputs.
     */
    fun getInterval(from: LocalDateTime, to: LocalDateTime, amountOfPoints: Int = 100,
                    type: AggregationType = AggregationType.AVG): List<Count> {
        /* Get all the count objects in the provided interval */
        val counts: List<Count> = countRepository.getInterval(from, to)
        if (counts.isEmpty()) {
            return listOf()
        }

        /* Calculate the amount of count objects per data point and chunk it */
        val amountOfCountsPerPoint = ceil(counts.size / amountOfPoints.toDouble())
        val chunks: List<List<Count>> = counts.chunked(amountOfCountsPerPoint.toInt())
        return when (type) {
            AggregationType.AVG -> {
                chunks.mapNotNull { chunk ->
                    averageCount(chunk)
                }
            }
            AggregationType.MIN -> {
                chunks.mapNotNull { chunk ->
                    minCount(chunk)
                }
            }
            AggregationType.MAX -> {
                chunks.mapNotNull { chunk ->
                    maxCount(chunk)
                }
            }
        }
    }

    /**
     * Creates an average count using the provided list of counts.
     *
     * @return A single count object that represents an average of the provided count objects.
     */
    private fun averageCount(counts: List<Count>): Count? {
        if (counts.isEmpty()) {
            return null
        }

        val compositeComponents: MutableList<CompositeCountComponent> = mutableListOf()
        val components = counts.fold(compositeComponents) { acc, count ->
            count.components.forEach { component ->
                val accComponent = acc.find { it.name == component.name }
                if (accComponent != null) {
                    accComponent.add(component)
                } else {
                    acc.add(CompositeCountComponent(component))
                }
            }

            return@fold acc
        }.map { it.toCount() }

        return Count(timestamp = counts[0].timestamp, components = components.toMutableList())
    }

    /**
     * Creates a maximum count using the provided list of counts.
     *
     * @return A single count object that represents a maximum of the provided count objects.
     */
    private fun maxCount(counts: List<Count>): Count? {
        if (counts.isEmpty()) {
            return null
        }

        return counts.fold(counts[0]) { acc, count ->
            count.components.forEach { component ->
                val accComponent = acc.components.find { it.name == component.name }
                if (accComponent != null) {
                    if (component.value > accComponent.value) {
                        accComponent.value = component.value
                    }
                }
            }

            return@fold acc
        }
    }

    /**
     * Creates a minimum count using the provided list of counts.
     *
     * @return A single count object that represents a minimum of the provided count objects.
     */
    private fun minCount(counts: List<Count>): Count? {
        if (counts.isEmpty()) {
            return null
        }

        return counts.fold(counts[0]) { acc, count ->
            count.components.forEach { component ->
                val accComponent = acc.components.find { it.name == component.name }
                if (accComponent != null) {
                    if (component.value < accComponent.value) {
                        accComponent.value = component.value
                    }
                }
            }

            return@fold acc
        }
    }

}
