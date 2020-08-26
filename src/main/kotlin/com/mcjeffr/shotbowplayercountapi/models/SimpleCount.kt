package com.mcjeffr.shotbowplayercountapi.models

import java.time.LocalDateTime

/**
 * This class represents a simple count. A simple count is not a managed Entity, it is simply a
 * combination of timestamp, name and value. This is used by the JPA layer as the response objects
 * from certain queries.
 *
 * @author McJeffr
 */
data class SimpleCount(
        val timestamp: LocalDateTime,
        val name: String,
        val value: Int
) {

    override fun toString(): String {
        return "SimpleCount(timestamp=$timestamp, name='$name', value=$value)"
    }

}
