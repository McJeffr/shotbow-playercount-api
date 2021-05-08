package com.mcjeffr.shotbowplayercountapi.utils

import com.mcjeffr.shotbowplayercountapi.models.Count
import com.mcjeffr.shotbowplayercountapi.models.CountComponent
import com.mcjeffr.shotbowplayercountapi.models.SimpleCount
import java.time.Instant

/**
 * This class contains a converter that can be used to turn a list of SimpleCount objects into a
 * list of regular Count objects.
 *
 * @author McJeffr
 */
class SimpleCountConverter {

    companion object {
        /**
         * Converts a list of SimpleCount objects into a list of Count objects.
         *
         * @param simpleCounts The list of SimpleCount objects.
         * @return A list of Count objects.
         */
        fun convertSimpleCounts(simpleCounts: List<SimpleCount>): List<Count> {
            return simpleCounts.fold(mutableMapOf<Instant, MutableList<SimpleCount>>()) { acc, simpleCount ->
                val list = acc.getOrDefault(simpleCount.timestamp, mutableListOf())
                list.add(simpleCount)
                acc[simpleCount.timestamp] = list
                acc
            }.map { entry: Map.Entry<Instant, MutableList<SimpleCount>> ->
                val components = entry.value.map { simpleCount ->
                    CountComponent(name = simpleCount.name, value = simpleCount.value)
                }
                Count(timestamp = entry.key, components = components.toMutableList())
            }
        }
    }

}
