package com.mcjeffr.shotbowplayercountapi.models

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.mcjeffr.shotbowplayercountapi.utils.CountDeserializer
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.persistence.*

/**
 * This class represents all the recorded counts at a specific point in time. Big brain
 * JPA-annotations time!
 *
 * @author McJeffr
 */
@Entity
@Table(name = "player_count")
@SqlResultSetMapping(name = "intervalDetailsMapping", classes = (
        arrayOf(
                ConstructorResult(
                        targetClass = SimpleCount::class,
                        columns = (
                                arrayOf(
                                        ColumnResult(name = "moment", type = LocalDateTime::class),
                                        ColumnResult(name = "name", type = String::class),
                                        ColumnResult(name = "value", type = Int::class)
                                ))
                ))
        ))
@NamedNativeQueries(
        NamedNativeQuery(name = "Count.getIntervalAvg", resultSetMapping = "intervalDetailsMapping", query = """
            SELECT date_trunc('seconds', to_timestamp(avg(extract(epoch from pc.moment)))) "moment",
                   pcc.name                                                                "name",
                   round(avg(pcc.value))                                                   "value"
            FROM player_count "pc"
                     INNER JOIN player_count_components "pccs" ON pc.id = pccs.count_id
                     INNER JOIN player_count_component "pcc" ON pccs.components_id = pcc.id
                     INNER JOIN (SELECT id, (ntile(:points) OVER (ORDER BY moment)) "tile"
                                 FROM player_count
                                 WHERE date_trunc('seconds', moment) BETWEEN :from AND :to) tiles
                                ON tiles.id = pc.id
            GROUP BY tiles.tile, pcc.name
            ORDER BY tiles.tile;
    """),
        NamedNativeQuery(name = "Count.getIntervalMin", resultSetMapping = "intervalDetailsMapping", query = """
            SELECT date_trunc('seconds', to_timestamp(avg(extract(epoch from pc.moment)))) "moment",
                   pcc.name                                                                "name",
                   min(pcc.value)                                                          "value"
            FROM player_count "pc"
                     INNER JOIN player_count_components "pccs" ON pc.id = pccs.count_id
                     INNER JOIN player_count_component "pcc" ON pccs.components_id = pcc.id
                     INNER JOIN (SELECT id, (ntile(:points) OVER (ORDER BY moment)) "tile"
                                 FROM player_count
                                 WHERE date_trunc('seconds', moment) BETWEEN :from AND :to) tiles
                                ON tiles.id = pc.id
            GROUP BY tiles.tile, pcc.name
            ORDER BY tiles.tile;
    """),
        NamedNativeQuery(name = "Count.getIntervalMax", resultSetMapping = "intervalDetailsMapping", query = """
            SELECT date_trunc('seconds', to_timestamp(avg(extract(epoch from pc.moment)))) "moment",
                   pcc.name                                                                "name",
                   max(pcc.value)                                                          "value"
            FROM player_count "pc"
                     INNER JOIN player_count_components "pccs" ON pc.id = pccs.count_id
                     INNER JOIN player_count_component "pcc" ON pccs.components_id = pcc.id
                     INNER JOIN (SELECT id, (ntile(:points) OVER (ORDER BY moment)) "tile"
                                 FROM player_count
                                 WHERE date_trunc('seconds', moment) BETWEEN :from AND :to) tiles
                                ON tiles.id = pc.id
            GROUP BY tiles.tile, pcc.name
            ORDER BY tiles.tile;
    """)
)
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
