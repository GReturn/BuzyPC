package io.buzypc.app.data.pc.components

import io.buzypc.app.data.buildpc.Store
import io.buzypc.app.data.pc.Component

class MotherboardComponent(
    val chipset: String,
    val socketType: String,
    val ramSlots: Int,
    val pciSlots: Int,
    name: String,
    brand: String,
    price: Double,
    performanceScore: Float,
    stores: List<Store>
) : Component(name, brand, price, performanceScore, stores) {
    var compatibilityScore: Int = 0
}