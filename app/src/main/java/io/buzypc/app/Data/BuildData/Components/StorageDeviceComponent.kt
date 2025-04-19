package io.buzypc.app.Data.BuildData.Components

import io.buzypc.app.Data.BuildData.Component
import io.buzypc.app.Data.BuildData.Store

class StorageDeviceComponent(
    val storageType: String,
    val writeSpeedMBps: Int,
    val readSpeedMBps: Int,
    val capacityGB: Int,
    name: String,
    brand: String,
    price: Double,
    performanceScore: Float,
    stores: List<Store>
) : Component(name, brand, price, performanceScore, stores) {

    lateinit var motherboard : MotherboardComponent
}