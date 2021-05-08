package com.mcjeffr.shotbowplayercountapi.models

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.mcjeffr.shotbowplayercountapi.utils.CountDeserializer
import java.time.Instant

/**
 * This class represents all the recorded counts at a specific point in time.
 *
 * @author McJeffr
 */
@JsonDeserialize(using = CountDeserializer::class)
class Count(
    val timestamp: Instant = Instant.now(),
    val components: MutableList<CountComponent> = mutableListOf()
) {

    override fun toString(): String {
        return "Count(timestamp=$timestamp, components=$components)"
    }

}
