package io.buzypc.app.data.pc

import io.buzypc.app.data.buildpc.Store


data class PC(
    val name: String,
    val motherboardName: String,
    val cpuName: String,
    val gpuName: String,
    val storageDeviceName: String,
    val ramName: String,
    val psuName: String,
    val cpuPrice: Double,
    val gpuPrice: Double,
    val storageDevicePrice: Double,
    val ramPrice: Double,
    val psuPrice: Double,
    val motherboardPrice: Double,
    val computingPower: Double,
    val graphicsRendering: Double,
    val dataStorage: Double,
    val dataTransferSpeed: Double,
    val powerCapacity: Double,
    val cpuCompatibility: Double,
    val gpuCompatibility: Double,
    val storageDeviceCompatibility: Double,
    val ramCompatibility: Double,
    val psuCompatibility: Double,
    val localStores: List<Store>
) {
    val totalPrice: Double
        get() = cpuPrice + gpuPrice + storageDevicePrice + ramPrice + psuPrice + motherboardPrice

    val overallPerformanceScore: Double
        get() = (computingPower + graphicsRendering + dataStorage + dataTransferSpeed + powerCapacity) / 5

    val compatibilityScore: Double
        get() = (cpuCompatibility + gpuCompatibility + storageDeviceCompatibility + ramCompatibility + psuCompatibility) / 5
}