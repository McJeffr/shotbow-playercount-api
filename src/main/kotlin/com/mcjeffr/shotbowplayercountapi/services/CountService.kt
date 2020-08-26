package com.mcjeffr.shotbowplayercountapi.services

import com.mcjeffr.shotbowplayercountapi.models.AggregationType
import com.mcjeffr.shotbowplayercountapi.models.Count
import com.mcjeffr.shotbowplayercountapi.repositories.CountRepository
import com.mcjeffr.shotbowplayercountapi.utils.SimpleCountConverter
import org.springframework.stereotype.Service
import java.time.LocalDateTime

/**
 * This class contains the count service. This service is responsible for dealing with retrieving
 * and processing count objects.
 *
 * @author McJeffr
 */
@Service
class CountService(private val countRepository: CountRepository) {

    /**
     * Gets the most recent count object that was inserted into the database.
     *
     * @return The most recent count object that was inserted into the database.
     */
    fun getLatest(): Count {
        return countRepository.getLatest()
    }

    /**
     * Gets a list of count objects that span the provided interval. No validation is taking place
     * to verify that the provided interval is correct, not if the parameters follow the constraints
     * that have been set in place.
     *
     * @param from The timestamp to start at.
     * @param to The timestamp to end at.
     * @param amountOfPoints The amount of data points to return. Defaults to 100.
     * @param type The aggregation type. Defaults to AVG.
     * @return A list of count components based on the provided inputs.
     */
    fun getInterval(from: LocalDateTime, to: LocalDateTime, amountOfPoints: Int = 100,
                    type: AggregationType = AggregationType.AVG): List<Count> {
        return when (type) {
            AggregationType.AVG -> {
                SimpleCountConverter.convertSimpleCounts(
                        countRepository.getIntervalAvg(from, to, amountOfPoints))
            }
            AggregationType.MIN -> {
                SimpleCountConverter.convertSimpleCounts(
                        countRepository.getIntervalMin(from, to, amountOfPoints))
            }
            AggregationType.MAX -> {
                SimpleCountConverter.convertSimpleCounts(
                        countRepository.getIntervalMax(from, to, amountOfPoints))
            }
        }
    }

}
