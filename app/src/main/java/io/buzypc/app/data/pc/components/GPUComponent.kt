package io.buzypc.app.data.pc.components

import io.buzypc.app.data.buildpc.Store
import io.buzypc.app.data.pc.Component

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