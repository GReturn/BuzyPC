package io.buzypc.app.Data.BuildData.Components

import io.buzypc.app.Data.BuildData.Component
import io.buzypc.app.Data.BuildData.Store

class GPUComponent(
    val vramSize: Int,
    val coreClockMHz: Float,
    val memoryClockMHz: Float,
    name: String,
    brand: String,
    price: Double,
    performanceScore: Float,
    stores: List<Store>
) : Component(name, brand, price, performanceScore, stores) {

    lateinit var motherboard : MotherboardComponent
}