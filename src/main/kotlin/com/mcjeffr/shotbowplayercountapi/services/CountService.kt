package com.mcjeffr.shotbowplayercountapi.services

import com.mcjeffr.shotbowplayercountapi.models.AggregationType
import com.mcjeffr.shotbowplayercountapi.models.CompositeCountComponent
import com.mcjeffr.shotbowplayercountapi.models.Count
import com.mcjeffr.shotbowplayercountapi.repositories.CountRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.math.ceil

@Service
class CountService(private val countRepository: CountRepository) {

    fun getLatest(): Count {
        return countRepository.getLatest()
    }

    fun getInterval(from: LocalDateTime, to: LocalDateTime, amountOfPoints: Int,
                    type: AggregationType): List<Count> {
        val counts: List<Count> = countRepository.getInterval(from, to)
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
