package io.buzypc.app.Data.BuildData

import io.buzypc.app.Data.BuildData.Components.CPUComponent
import io.buzypc.app.Data.BuildData.Components.GPUComponent
import io.buzypc.app.Data.BuildData.Components.MotherboardComponent
import io.buzypc.app.Data.BuildData.Components.PSUComponent
import io.buzypc.app.Data.BuildData.Components.RAMComponent
import io.buzypc.app.Data.BuildData.Components.StorageDeviceComponent
import io.buzypc.app.Data.BuildData.Utils.RatingLevel

data class PC(
    val motherboard: MotherboardComponent,
    val cpu: CPUComponent,
    val gpu: GPUComponent,
    val storageDevice: StorageDeviceComponent,
    val ram: RAMComponent,
    val psu: PSUComponent
) {
    fun getPurchasedComponentCount(): Pair<Int,Int> {
        val components = listOf(
            motherboard,
            cpu,
            gpu,
            storageDevice,
            ram,
            psu
        )
        var ctr = 0
        var total = 0
        for (component in components) {
            if (component.isBought) {
                ctr++
            }
            total++
        }
        return Pair(ctr, total)
    }

    fun getTotalPrice(): Double {
        val components = listOf(
            motherboard,
            cpu,
            gpu,
            storageDevice,
            ram,
            psu
        )
        var total = 0.0
        for (component in components) {
            total += component.price
        }
        return total
    }

    fun getOverallPerformanceScore(): Float {
        val components = listOf(
            motherboard,
            cpu,
            gpu,
            storageDevice,
            ram,
            psu
        )
        val totalScore = components.sumOf { it.performanceScore.toDouble() }
        return (totalScore / components.size).toFloat()
    }

    fun getOverallCompatibilityScore(): Float {
        val components = listOf(
            motherboard,
            cpu,
            gpu,
            storageDevice,
            ram,
            psu
        )
        val totalScore = components.sumOf { it.compatibilityScore.toDouble() }
        return (totalScore / components.size).toFloat()
    }

    fun getPerformanceRating(): RatingLevel {
        val avgScore = getOverallPerformanceScore()
        return mapScoreToRating(avgScore)
    }

    fun getCompatibilityRating(): RatingLevel {
        val avgScore = getOverallCompatibilityScore()
        return mapScoreToRating(avgScore)
    }

    private fun mapScoreToRating(score: Float): RatingLevel {
        return when {
            score < 25 -> RatingLevel.POOR
            score < 50 -> RatingLevel.AVERAGE
            score < 75 -> RatingLevel.GOOD
            else -> RatingLevel.EXCELLENT
        }
    }
}