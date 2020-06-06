package com.mcjeffr.shotbowplayercountapi.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "application")
class CustomConfigurationProperties(
        var endpoint: String = "",
        var maxIntervalDays: Int = 100
)
