package com.mcjeffr.shotbowplayercountapi.models

import java.time.Instant
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

/**
 * This class represents a flat count object. This flat count is used by GraphQL as its type. This
 * object is needed because GraphQL doesn't allow for dynamic types. It's a bit messy, but it works
 * fine for this use-case.
 *
 * @author McJeffr
 */
data class FlatCount(
        var timestamp: Instant,
        var all: Int = -1,
        var minez: Int = -1,
        var smash: Int = -1,
        var ghostcraft: Int = -1,
        var hcfactions: Int = -1,
        var annihilation: Int = -1,
        var lms: Int = -1,
        var wasted: Int = -1,
        var wasted_sandbox: Int = -1,
        var wasted_gungame: Int = -1,
        var wasted_control: Int = -1,
        var dbv: Int = -1,
        var craftybomber: Int = -1,
        var vadact: Int = -1,
        var wir: Int = -1,
        var minez2: Int = -1,
        var slaughter: Int = -1,
        var lightbikes: Int = -1,
        var hips: Int = -1,
        var mama: Int = -1,
        var sweepers: Int = -1,
        var lobby: Int = -1,
        var gg: Int = -1,
        var flappy: Int = -1,
        var assault: Int = -1,
        var goldrush: Int = -1,
        var mta: Int = -1,
        var civwar: Int = -1,
        var ddg: Int = -1,
        var shotball: Int = -1,
        var warband: Int = -1,
        var minerproblems: Int = -1,
) {

    companion object {

        /**
         * Constructs a flat count object from a regular count object. Uses reflection to make it
         * not too messy.
         * @param count The count object to convert to a flat count object.
         * @return A flat count object created from the provided count object.
         */
        fun fromCount(count: Count): FlatCount {
            val flatCount = FlatCount(timestamp = count.timestamp)
            count.components.forEach { component ->
                /* Fetch the property from the class (aka the attribute) */
                val prop = FlatCount::class.memberProperties.find { it.name == component.name }

                /* If the property exists and is mutable, set it to the component's value */
                if (prop != null && prop is KMutableProperty<*>) {
                    prop.setter.call(flatCount, component.value)
                }
            }
            return flatCount
        }

    }

}
