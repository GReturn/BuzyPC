package io.buzypc.app.Data.BuildData

import java.util.Date

data class PCBuild(
    val id: Int,

    val name: String,
    val budget: Double,
    val pc: PC,

    val createdAt: Date = Date(),
    val isTracked: Boolean = false,
    val isArchived: Boolean = false,
    val isDeleted: Boolean = false
) {
    fun getProgress(): Pair<Int,Int> {
        return pc.getPurchasedComponentCount()
    }
}
