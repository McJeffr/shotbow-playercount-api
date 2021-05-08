package com.mcjeffr.shotbowplayercountapi.models

/**
 * This class represents a count component. A count component is a part of a full set of all counts.
 * It effectively represents the player count of a specific game at a specific time. This component
 * does not contain a timestamp, as it should be part of a wrapper that contains a collection of
 * counts. See @link{Count}.
 *
 * @author McJeffr
 */
open class CountComponent(
    open var name: String,
    open var value: Int
) {

    override fun toString(): String {
        return "CountComponent(name='$name', value=$value)"
    }

}
