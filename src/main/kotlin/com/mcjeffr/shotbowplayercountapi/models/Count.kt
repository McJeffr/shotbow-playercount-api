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
        val total: Int = -1,

        @Column(name = "lobby")
        @JsonProperty("lobby")
        val lobby: Int = -1,

        @Column(name = "minez")
        @JsonProperty("minez")
        val minez: Int = -1,

        @Column(name = "annihilation")
        @JsonProperty("annihilation")
        val annihilation: Int = -1,

        @Column(name = "smash")
        @JsonProperty("smash")
        val smash: Int = -1,

        @Column(name = "slaughter")
        @JsonProperty("slaughter")
        val slaughter: Int = -1,

        @Column(name = "dbv")
        @JsonProperty("dbv")
        val dbv: Int = -1,

        @Column(name = "gg")
        @JsonProperty("gg")
        val gg: Int = -1,

        @Column(name = "mta")
        @JsonProperty("mta")
        val mta: Int = -1,

        @Column(name = "wir")
        @JsonProperty("wir")
        val wir: Int = -1,

        @Column(name = "ghostcraft")
        @JsonProperty("ghostcraft")
        val ghostcraft: Int = -1,

        @Column(name = "wasted_sandbox")
        @JsonProperty("wasted_sandbox")
        val wastedSandbox: Int = -1,

        @Column(name = "wasted_gungame")
        @JsonProperty("wasted_gungame")
        val wastedGungame: Int = -1,

        @Column(name = "minez2")
        @JsonProperty("minez2")
        val minez2: Int = -1,

        @Column(name = "goldrush")
        @JsonProperty("goldrush")
        val goldrush: Int = -1
)
