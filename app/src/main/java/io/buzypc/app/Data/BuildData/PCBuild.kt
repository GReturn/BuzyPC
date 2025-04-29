package io.buzypc.app.Data.BuildData

import io.buzypc.app.Data.BuildData.Utils.PerformanceToBudgetRating
import java.util.Date

data class PCBuild(
    val id: Int,

    var name: String,
    val budget: Double,
    val pc: PC,

    val createdAt: Date = Date(),
    var isTracked: Boolean = false,
    var isArchived: Boolean = false,
    var isDeleted: Boolean = false
) {
    fun getProgress(): Pair<Int,Int> {
        return pc.getPurchasedComponentCount()
    }

    fun getTotalPrice(): Double {
        return pc.getTotalPrice()
    }

    fun getPerformanceToBudgetRatio(): Float {
        val performance = pc.getOverallPerformanceScore()
        return if (budget > 0) performance / budget.toFloat() else 0f
    }

    fun getPerformanceToBudgetRating(): PerformanceToBudgetRating {
        val ratio = getPerformanceToBudgetRatio()
        return when {
            ratio < 0.5f -> PerformanceToBudgetRating.UNDER_BUDGET_LOW_PERFORMANCE
            ratio < 1.0f -> PerformanceToBudgetRating.WELL_BALANCED
            ratio < 2.0f -> PerformanceToBudgetRating.PERFORMANCE_FOCUSED
            else -> PerformanceToBudgetRating.OVERSPENT_LOW_RETURN
        }
    }
}
