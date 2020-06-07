package com.mcjeffr.shotbowplayercountapi.utils

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.mcjeffr.shotbowplayercountapi.models.Count
import com.mcjeffr.shotbowplayercountapi.models.CountComponent
import java.lang.Integer.parseInt
import java.lang.NumberFormatException

class CountDeserializer : JsonDeserializer<Count>() {

    override fun deserialize(parser: JsonParser, context: DeserializationContext): Count {
        val node: JsonNode = parser.readValueAsTree()

        val count = Count()
        node.fieldNames().forEach { name ->
            val value = node.get(name)
            try {
                count.components.add(CountComponent(name = name, value = parseInt(value.asText())))
            } catch (ignored: NumberFormatException) {
            }
        }

        return count
    }

}
