package com.mcjeffr.shotbowplayercountapi.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * This class contains the application's configuration properties from the configuration files.
 *
 * @author McJeffr
 */
@Component
@ConfigurationProperties(prefix = "application")
class CustomConfigurationProperties(
        var endpoint: String = "",
        var maxIntervalDays: Int = 100,
        var maxPoints: Int = 1000
)
