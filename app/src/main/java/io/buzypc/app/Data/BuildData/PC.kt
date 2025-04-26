package io.buzypc.app.Data.BuildData

import io.buzypc.app.Data.BuildData.Components.CPUComponent
import io.buzypc.app.Data.BuildData.Components.GPUComponent
import io.buzypc.app.Data.BuildData.Components.MotherboardComponent
import io.buzypc.app.Data.BuildData.Components.PSUComponent
import io.buzypc.app.Data.BuildData.Components.RAMComponent
import io.buzypc.app.Data.BuildData.Components.StorageDeviceComponent

data class PC(
    val motherboard: MotherboardComponent,
    val cpu: CPUComponent,
    val gpu: GPUComponent,
    val storageDevice: StorageDeviceComponent,
    val ram: RAMComponent,
    val psu: PSUComponent
) {
    fun getPurchasedComponentCount(): Pair<Int,Int> {
        val components = listOf(
            motherboard,
            cpu,
            gpu,
            storageDevice,
            ram,
            psu
        )
        var ctr = 0
        var total = 0
        for (component in components) {
            if (component.isBought) {
                ctr++
            }
            total++
        }
        return Pair(ctr, total)
    }

    fun getTotalPrice(): Double {
        val components = listOf(
            motherboard,
            cpu,
            gpu,
            storageDevice,
            ram,
            psu
        )
        var total = 0.0
        for (component in components) {
            total += component.price
        }
        return total
    }
}