package com.mcjeffr.shotbowplayercountapi.repositories

import com.mcjeffr.shotbowplayercountapi.models.Count
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CountRepository : JpaRepository<Count, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM playercount ORDER BY moment DESC LIMIT 1")
    fun getLatest(): Count

    @Query(nativeQuery = true, value = "SELECT * FROM playercount WHERE date_trunc('milliseconds', moment) BETWEEN ?1 AND ?2")
    fun getInterval(from: Date, to: Date): List<Count>

}
