package com.mcjeffr.shotbowplayercountapi.runners

import com.mcjeffr.shotbowplayercountapi.models.Count
import com.mcjeffr.shotbowplayercountapi.repositories.CountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.*

@Component
class CountScraper @Autowired constructor(private val countRepository: CountRepository) {

    @Value("\${endpoint}")
    private lateinit var endpoint: String

    private final val restTemplate = RestTemplate()

    @Scheduled(fixedRate = 15000)
    fun scrapeCounts() {
        val count: Count? = restTemplate.getForObject(endpoint, Count::class.java)
        if (count != null) {
            count.timestamp = Date((count.timestamp.time / 1000) * 1000) // Shave off the milliseconds
            countRepository.save(count)
        }
    }

}
