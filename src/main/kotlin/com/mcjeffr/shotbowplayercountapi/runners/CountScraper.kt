package com.mcjeffr.shotbowplayercountapi.runners

import com.mcjeffr.shotbowplayercountapi.configuration.CustomConfigurationProperties
import com.mcjeffr.shotbowplayercountapi.models.Count
import com.mcjeffr.shotbowplayercountapi.repositories.CountRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

/**
 * This class contains the scraper that scrapes the player count endpoint from Shotbow at a fixed
 * interval.
 *
 * @author McJeffr
 */
@Component
class CountScraper @Autowired constructor(
        private val config: CustomConfigurationProperties,
        private val countRepository: CountRepository
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CountScraper::class.java)
    }

    private final val restTemplate = RestTemplate()

    @Scheduled(fixedRate = 15000)
    fun scrapeCounts() {
        try {
            val count: Count? = restTemplate.getForObject(config.endpoint, Count::class.java)
            if (count != null) {
                countRepository.save(count)
            }
        } catch (e: Exception) {
            logger.error("Failed to scrape count object", e)
        }
    }

}
