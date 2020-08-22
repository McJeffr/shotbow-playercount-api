package com.mcjeffr.shotbowplayercountapi.repositories

import com.mcjeffr.shotbowplayercountapi.models.Count
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
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

    @Query(nativeQuery = true, value = "SELECT * FROM player_count WHERE date_trunc('milliseconds', moment) BETWEEN ?1 AND ?2")
    fun getInterval(from: LocalDateTime, to: LocalDateTime): List<Count>

}
