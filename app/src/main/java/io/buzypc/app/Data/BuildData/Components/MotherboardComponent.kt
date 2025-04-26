package io.buzypc.app.Data.BuildData.Components

import io.buzypc.app.Data.BuildData.Component
import io.buzypc.app.Data.BuildData.Store

class MotherboardComponent(
    val chipset: String,
    val socketType: String,
    val ramSlots: Int,
    val pciSlots: Int,
    name: String,
    brand: String,
    price: Double,
    performanceScore: Float,
    compatibilityScore: Float,
    stores: List<Store>
) : Component(name, brand, price, performanceScore, compatibilityScore, stores)