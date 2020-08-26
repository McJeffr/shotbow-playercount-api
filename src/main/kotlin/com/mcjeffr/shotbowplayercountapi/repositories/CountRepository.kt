package com.mcjeffr.shotbowplayercountapi.repositories

import com.mcjeffr.shotbowplayercountapi.models.Count
import com.mcjeffr.shotbowplayercountapi.models.SimpleCount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 * This interface is used as the container for communication with the database.
 *
 * @author McJeffr
 */
@Repository
interface CountRepository : JpaRepository<Count, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM player_count ORDER BY moment DESC LIMIT 1")
    fun getLatest(): Count

    @Query(nativeQuery = true)
    fun getIntervalAvg(@Param("from") from: LocalDateTime, @Param("to") to: LocalDateTime,
                       @Param("points") points: Int): List<SimpleCount>

    @Query(nativeQuery = true)
    fun getIntervalMin(@Param("from") from: LocalDateTime, @Param("to") to: LocalDateTime,
                       @Param("points") points: Int): List<SimpleCount>

    @Query(nativeQuery = true)
    fun getIntervalMax(@Param("from") from: LocalDateTime, @Param("to") to: LocalDateTime,
                       @Param("points") points: Int): List<SimpleCount>

}
