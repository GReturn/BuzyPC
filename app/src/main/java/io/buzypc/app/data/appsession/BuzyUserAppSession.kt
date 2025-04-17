package io.buzypc.app.data.appsession

import android.app.Application
import io.buzypc.app.data.buildpc.Store
import io.buzypc.app.data.pc.PC

class BuzyUserAppSession: Application() {
    var username: String = ""
    var theme: String = ""

    var buildBudget: String = ""
    var buildName: String = ""

    override fun onCreate() {
        super.onCreate()
    }
    val myPC = PC(
        name = "Budget Beast ₱20K",
        motherboardName       = "ASRock A320M‑HDV R4.0",
        cpuName               = "AMD Ryzen 3 3200G (Vega 8 iGPU)",
        gpuName               = "Integrated Radeon Vega 8",
        storageDeviceName     = "Kingston A400 240 GB SSD",
        ramName               = "Crucial 8 GB DDR4‑2666MHz",
        psuName               = "FSP Hydro K 450 W 80+ Bronze",

        // component prices (PHP)
        cpuPrice              = 5500.00,
        gpuPrice              = 0.00,      // using integrated graphics
        motherboardPrice      = 3500.00,
        storageDevicePrice    = 1800.00,
        ramPrice              = 1800.00,
        psuPrice              = 1900.00,

        // performance metrics (scaled-down from high-end)
        computingPower        = 4.0,
        graphicsRendering     = 4.0,
        dataStorage           = 4.5,
        dataTransferSpeed     = 5.0,
        powerCapacity         = 5.0,

        // compatibility (1.0 = perfect)
        cpuCompatibility      = 4.0,
        gpuCompatibility      = 5.0,
        storageDeviceCompatibility = 4.0,
        ramCompatibility      = 3.5,
        psuCompatibility      = 4.5,

        localStores = listOf(
            Store(
                name         = "PC Express – Cebu (CPU)",
                imageUrl     = "https://www.pcexpress.com.ph/image/cache/catalog/products/amd/amd-ryzen-3-3200g-500x500.jpg",
                vendorSite   = "https://www.pcexpress.com.ph/",
                associatedItem = "AMD Ryzen 3 3200G",
                priceRange   = 5500.00
            ),
            Store(
                name         = "Silicon Valley – Cebu (Motherboard)",
                imageUrl     = "https://www.siliconvalley.com.ph/cdn/shop/products/ASROCKA320MHDV_1_1024x1024.png",
                vendorSite   = "https://www.siliconvalley.com.ph/",
                associatedItem = "ASRock A320M-HDV R4.0",
                priceRange   = 3500.00
            ),
            Store(
                name         = "EasyPC – Mandaue (SSD)",
                imageUrl     = "https://easypc.com.ph/image/cache/catalog/ssd/kingston/240gb/kingston-a400-240gb-01-500x500.jpg",
                vendorSite   = "https://easypc.com.ph/",
                associatedItem = "Kingston A400 240 GB SSD",
                priceRange   = 1800.00
            ),
            Store(
                name         = "PC Corner – Liloan (RAM)",
                imageUrl     = "https://www.pccorner.com.ph/image/cache/catalog/components/memory/crucial/crucial-8gb-ddr4-2666mhz-01-500x500.jpg",
                vendorSite   = "https://www.pccorner.com.ph/",
                associatedItem = "Crucial 8 GB DDR4‑2666MHz",
                priceRange   = 1800.00
            ),
            Store(
                name         = "Techwarez – Cebu (PSU)",
                imageUrl     = "https://techwarez.com.ph/image/cache/catalog/power%20supply/fsp/hydro-k-450w/fsp-hydro-k-450w-01-500x500.jpg",
                vendorSite   = "https://techwarez.com.ph/",
                associatedItem = "FSP Hydro K 450 W 80+ Bronze",
                priceRange   = 1900.00
            )
        )
    )
}
