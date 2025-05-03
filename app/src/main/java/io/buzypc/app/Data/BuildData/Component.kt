package io.buzypc.app.Data.BuildData

open class Component(
    val name: String,
    val brand: String,
    val price: Double,
    val performanceScore: Float,
    val stores: List<Store>,

    var isBought: Boolean = false
)

