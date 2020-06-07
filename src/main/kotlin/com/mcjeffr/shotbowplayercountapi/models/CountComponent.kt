package com.mcjeffr.shotbowplayercountapi.models

import javax.persistence.*

/**
 * This class represents a count component. A count component is a part of a full set of all counts.
 * It effectively represents the player count of a specific game at a specific time. This component
 * does not contain a timestamp, as it should be part of a wrapper that contains a collection of
 * counts. See @link{Count}.
 *
 * @author McJeffr
 */
@Entity
@Table(name = "player_count_component")
open class CountComponent(
        @Id
        @GeneratedValue
        @Column(name = "id")
        open var id: Long = 0,

        @Column(name = "name")
        open var name: String,

        @Column(name = "value")
        open var value: Int
) {

    override fun toString(): String {
        return "CountComponent(name='$name', value=$value)"
    }

}
