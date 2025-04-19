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
)