package io.buzypc.app.Data.BuildData

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
}
