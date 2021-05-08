package com.mcjeffr.shotbowplayercountapi.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "influx")
class InfluxConfigurationProperties(
    var url: String = "",
    var token: String = "",
    var org: String = "",
    var bucket: String = ""
)
