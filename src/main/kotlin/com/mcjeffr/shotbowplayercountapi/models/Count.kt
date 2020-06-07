package com.mcjeffr.shotbowplayercountapi.models

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.mcjeffr.shotbowplayercountapi.utils.CountDeserializer
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.persistence.*

/**
 * This class represents all the recorded counts at a specific point in time.
 *
 * @author McJeffr
 */
@Entity
@Table(name = "player_count")
@JsonDeserialize(using = CountDeserializer::class)
class Count(
        @Id
        @GeneratedValue
        @Column(name = "id")
        var id: Long = 0,

        @Column(name = "moment")
        val timestamp: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
                .truncatedTo(ChronoUnit.SECONDS),

        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        @JoinTable(name = "player_count_components")
        val components: MutableList<CountComponent> = mutableListOf()
) {

    override fun toString(): String {
        return "Count(id=$id, timestamp=$timestamp, components=$components)"
    }

}
