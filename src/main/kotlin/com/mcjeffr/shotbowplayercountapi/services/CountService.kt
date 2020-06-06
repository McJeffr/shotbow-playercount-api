package com.mcjeffr.shotbowplayercountapi.services

import com.mcjeffr.shotbowplayercountapi.models.AggregationType
import com.mcjeffr.shotbowplayercountapi.models.Count
import com.mcjeffr.shotbowplayercountapi.repositories.CountRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.math.ceil

@Service
class CountService(private val countRepository: CountRepository) {

    fun getLatest(): Count {
        return countRepository.getLatest()
    }

    fun getInterval(from: Date, to: Date, amountOfPoints: Int, type: AggregationType): List<Count> {
        val counts: List<Count> = countRepository.getInterval(from, to)
        val amountOfCountsPerPoint = ceil(counts.size / amountOfPoints.toDouble())

        val chunks: List<List<Count>> = counts.chunked(amountOfCountsPerPoint.toInt())
        return when (type) {
            AggregationType.AVG -> {
                chunks.map { chunk ->
                    averageCount(chunk)
                }
            }
            AggregationType.MIN -> {
                chunks.map { chunk ->
                    minCount(chunk)
                }
            }
            AggregationType.MAX -> {
                chunks.map { chunk ->
                    maxCount(chunk)
                }
            }
        }
    }

    private fun averageCount(counts: List<Count>): Count {
        /* Yucky code, a refactor of the database is desperately needed to prevent this */
        var totalAmount = 1
        var lobbyAmount = 1
        var minezAmount = 1
        var annihilationAmount = 1
        var smashAmount = 1
        var slaughterAmount = 1
        var dbvAmount = 1
        var ggAmount = 1
        var mtaAmount = 1
        var wirAmount = 1
        var ghostcraftAmount = 1
        var wastedSandboxAmount = 1
        var wastedGungameAmount = 1
        var goldrushAmount = 1

        val avgCount = counts.reduce { acc, count ->
            if (count.total >= 0) {
                acc.total += count.total
                totalAmount++
            }
            if (count.lobby >= 0) {
                acc.lobby += count.lobby
                lobbyAmount++
            }
            if (count.minez >= 0) {
                acc.minez += count.minez
                minezAmount++
            }
            if (count.annihilation >= 0) {
                acc.annihilation += count.annihilation
                annihilationAmount++
            }
            if (count.smash >= 0) {
                acc.smash += count.smash
                smashAmount++
            }
            if (count.slaughter >= 0) {
                acc.slaughter += count.slaughter
                slaughterAmount++
            }
            if (count.dbv >= 0) {
                acc.dbv += count.dbv
                dbvAmount++
            }
            if (count.gg >= 0) {
                acc.gg += count.gg
                ggAmount++
            }
            if (count.mta >= 0) {
                acc.mta += count.mta
                mtaAmount++
            }
            if (count.wir >= 0) {
                acc.wir += count.wir
                wirAmount++
            }
            if (count.ghostcraft >= 0) {
                acc.ghostcraft += count.ghostcraft
                ghostcraftAmount++
            }
            if (count.wastedSandbox >= 0) {
                acc.wastedSandbox += count.wastedSandbox
                wastedSandboxAmount++
            }
            if (count.wastedGungame >= 0) {
                acc.wastedGungame += count.wastedGungame
                wastedGungameAmount++
            }
            if (count.goldrush >= 0) {
                acc.goldrush += count.goldrush
                goldrushAmount++
            }

            return@reduce acc
        }

        avgCount.total = avgCount.total / totalAmount
        avgCount.lobby = avgCount.lobby / lobbyAmount
        avgCount.minez = avgCount.minez / minezAmount
        avgCount.annihilation = avgCount.annihilation / annihilationAmount
        avgCount.smash = avgCount.smash / smashAmount
        avgCount.slaughter = avgCount.slaughter / slaughterAmount
        avgCount.dbv = avgCount.dbv / dbvAmount
        avgCount.gg = avgCount.gg / ggAmount
        avgCount.mta = avgCount.mta / mtaAmount
        avgCount.wir = avgCount.wir / wirAmount
        avgCount.ghostcraft = avgCount.ghostcraft / ghostcraftAmount
        avgCount.wastedSandbox = avgCount.wastedSandbox / wastedSandboxAmount
        avgCount.wastedGungame = avgCount.wastedGungame / wastedGungameAmount
        avgCount.goldrush = avgCount.goldrush / goldrushAmount

        return avgCount
    }

    private fun maxCount(counts: List<Count>): Count {
        return counts.reduce { acc, count ->
            /* Yucky code, a refactor of the database is desperately needed to prevent this */
            acc.total = if (count.total > acc.total) count.total else acc.total
            acc.lobby = if (count.lobby > acc.lobby) count.lobby else acc.lobby
            acc.minez = if (count.minez > acc.minez) count.minez else acc.minez
            acc.annihilation = if (count.annihilation > acc.annihilation) count.annihilation else acc.annihilation
            acc.smash = if (count.smash > acc.smash) count.smash else acc.smash
            acc.slaughter = if (count.slaughter > acc.slaughter) count.slaughter else acc.slaughter
            acc.dbv = if (count.dbv > acc.dbv) count.dbv else acc.dbv
            acc.gg = if (count.gg > acc.gg) count.gg else acc.gg
            acc.mta = if (count.mta > acc.mta) count.mta else acc.mta
            acc.wir = if (count.wir > acc.wir) count.wir else acc.wir
            acc.ghostcraft = if (count.ghostcraft > acc.ghostcraft) count.ghostcraft else acc.ghostcraft
            acc.wastedSandbox = if (count.wastedSandbox > acc.wastedSandbox) count.wastedSandbox else acc.wastedSandbox
            acc.wastedGungame = if (count.wastedGungame > acc.wastedGungame) count.wastedGungame else acc.wastedGungame
            acc.goldrush = if (count.goldrush > acc.goldrush) count.goldrush else acc.goldrush

            return@reduce acc
        }
    }

    private fun minCount(counts: List<Count>): Count {
        return counts.reduce { acc, count ->
            /* Yucky code, a refactor of the database is desperately needed to prevent this */
            acc.total = if (count.total < acc.total) count.total else acc.total
            acc.lobby = if (count.lobby < acc.lobby) count.lobby else acc.lobby
            acc.minez = if (count.minez < acc.minez) count.minez else acc.minez
            acc.annihilation = if (count.annihilation < acc.annihilation) count.annihilation else acc.annihilation
            acc.smash = if (count.smash < acc.smash) count.smash else acc.smash
            acc.slaughter = if (count.slaughter < acc.slaughter) count.slaughter else acc.slaughter
            acc.dbv = if (count.dbv < acc.dbv) count.dbv else acc.dbv
            acc.gg = if (count.gg < acc.gg) count.gg else acc.gg
            acc.mta = if (count.mta < acc.mta) count.mta else acc.mta
            acc.wir = if (count.wir < acc.wir) count.wir else acc.wir
            acc.ghostcraft = if (count.ghostcraft < acc.ghostcraft) count.ghostcraft else acc.ghostcraft
            acc.wastedSandbox = if (count.wastedSandbox < acc.wastedSandbox) count.wastedSandbox else acc.wastedSandbox
            acc.wastedGungame = if (count.wastedGungame < acc.wastedGungame) count.wastedGungame else acc.wastedGungame
            acc.goldrush = if (count.goldrush < acc.goldrush) count.goldrush else acc.goldrush

            return@reduce acc
        }
    }

}
