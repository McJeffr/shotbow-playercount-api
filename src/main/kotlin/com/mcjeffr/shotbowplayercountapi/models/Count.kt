package com.mcjeffr.shotbowplayercountapi.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "playercount")
data class Count(
        @Id
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "moment")
        var timestamp: Date = Calendar.getInstance().time,

        @Column(name = "total")
        @JsonProperty("all")
        var total: Int = -1,

        @Column(name = "lobby")
        @JsonProperty("lobby")
        var lobby: Int = -1,

        @Column(name = "minez")
        @JsonProperty("minez")
        var minez: Int = -1,

        @Column(name = "annihilation")
        @JsonProperty("annihilation")
        var annihilation: Int = -1,

        @Column(name = "smash")
        @JsonProperty("smash")
        var smash: Int = -1,

        @Column(name = "slaughter")
        @JsonProperty("slaughter")
        var slaughter: Int = -1,

        @Column(name = "dbv")
        @JsonProperty("dbv")
        var dbv: Int = -1,

        @Column(name = "gg")
        @JsonProperty("gg")
        var gg: Int = -1,

        @Column(name = "mta")
        @JsonProperty("mta")
        var mta: Int = -1,

        @Column(name = "wir")
        @JsonProperty("wir")
        var wir: Int = -1,

        @Column(name = "ghostcraft")
        @JsonProperty("ghostcraft")
        var ghostcraft: Int = -1,

        @Column(name = "wasted_sandbox")
        @JsonProperty("wasted_sandbox")
        var wastedSandbox: Int = -1,

        @Column(name = "wasted_gungame")
        @JsonProperty("wasted_gungame")
        var wastedGungame: Int = -1,

        @Column(name = "minez2")
        @JsonProperty("minez2")
        var minez2: Int = -1,

        @Column(name = "goldrush")
        @JsonProperty("goldrush")
        var goldrush: Int = -1
)
