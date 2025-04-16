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
        name = "Gaming Beast",
        motherboardName = "ASUS ROG Strix Z790-E Gaming WiFi",
        cpuName = "Intel Core i9-14900K",
        gpuName = "NVIDIA GeForce RTX 4090",
        storageDeviceName = "Samsung 990 Pro 2TB NVMe SSD",
        ramName = "Corsair Vengeance DDR5 64GB (2x32GB) 6000MHz",
        psuName = "Corsair HX1200 1200W 80+ Platinum",
        cpuPrice = 700.00,
        gpuPrice = 1600.00,
        storageDevicePrice = 180.00,
        ramPrice = 220.00,
        psuPrice = 250.00,
        motherboardPrice = 450.00,
        computingPower = 9.8,
        graphicsRendering = 9.9,
        dataStorage = 2000.0,
        dataTransferSpeed = 7500.0,
        powerCapacity = 1200.0,
        cpuCompatibility = 1.0,
        gpuCompatibility = 1.0,
        storageDeviceCompatibility = 1.0,
        ramCompatibility = 1.0,
        psuCompatibility = 1.0,
        localStores = listOf(
            Store(
                name = "PC Express - Liloan (CPU)",
                imageUrl = "https://www.pcexpress.com.ph/image/cache/catalog/products/intel/intel%2014th%20gen/i9-14900k-500x500.jpg",
                vendorSite = "https://www.pcexpress.com.ph/",
                associatedItem = "Intel Core i9-14900K",
                priceRange = 700.00
            ),
            Store(
                name = "Silicon Valley - Cebu (GPU)",
                imageUrl = "https://www.siliconvalley.com.ph/cdn/shop/products/RTX4090SGOC24G_1_1024x1024.png?v=1666858882",
                vendorSite = "https://www.siliconvalley.com.ph/",
                associatedItem = "NVIDIA GeForce RTX 4090",
                priceRange = 1600.00
            ),
            Store(
                name = "EasyPC - Mandaue (SSD)",
                imageUrl = "https://easypc.com.ph/image/cache/catalog/ssd/samsung/990-pro/samsung-990-pro-nvme-pcie-gen4-ssd-2tb-01-500x500.jpg",
                vendorSite = "https://easypc.com.ph/",
                associatedItem = "Samsung 990 Pro 2TB NVMe SSD",
                priceRange = 180.00
            ),
            Store(
                name = "PC Corner - Liloan (RAM)",
                imageUrl = "https://www.pccorner.com.ph/image/cache/catalog/components/memory/corsair/vengeance-ddr5/corsair-vengeance-ddr5-64gb-2x32gb-6000mhz-black-01-500x500.jpg",
                vendorSite = "https://www.pccorner.com.ph/",
                associatedItem = "Corsair Vengeance DDR5 64GB (2x32GB) 6000MHz",
                priceRange = 220.00
            ),
            Store(
                name = "Techwarez - Cebu (PSU)",
                imageUrl = "https://techwarez.com.ph/image/cache/catalog/power%20supply/corsair/hx1200/corsair-hx1200-1200w-80-plus-platinum-fully-modular-power-supply-01-500x500.jpg",
                vendorSite = "https://techwarez.com.ph/",
                associatedItem = "Corsair HX1200 1200W 80+ Platinum",
                priceRange = 250.00
            ),
            Store(
                name = "Ayala Center Cebu - Motherboard",
                imageUrl = "https://dlcdnweb.asus.com/gain/69892374-b63a-4979-8782-817984814c88/w800",
                vendorSite = "https://ph.store.asus.com/", // Example ASUS store
                associatedItem = "ASUS ROG Strix Z790-E Gaming WiFi",
                priceRange = 450.00
            )
        )
    )
}
