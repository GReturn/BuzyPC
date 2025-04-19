package io.buzypc.app.data.pc.components

import io.buzypc.app.data.buildpc.Store
import io.buzypc.app.data.pc.Component

class RAMComponent(
    val capacityGB: Int,
    val speedMHz: Int,
    val ddrType: String,
    name: String,
    brand: String,
    price: Double,
    performanceScore: Float,
    stores: List<Store>
) : Component(name, brand, price, performanceScore, stores) {

    lateinit var motherboard : MotherboardComponent
}