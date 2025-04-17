package io.buzypc.app.data.pc

import io.buzypc.app.data.buildpc.Store
import io.buzypc.app.data.pc.components.CPUComponent
import io.buzypc.app.data.pc.components.GPUComponent
import io.buzypc.app.data.pc.components.MotherboardComponent
import io.buzypc.app.data.pc.components.PSUComponent
import io.buzypc.app.data.pc.components.RAMComponent
import io.buzypc.app.data.pc.components.StorageDeviceComponent


data class PC(
    val name: String,
    val motherboard: MotherboardComponent,
    val cpu: CPUComponent,
    val gpu: GPUComponent,
    val storageDevice: StorageDeviceComponent,
    val ram: RAMComponent,
    val psu: PSUComponent
) {}