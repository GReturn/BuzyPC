package io.buzypc.app.Data.AppSession

import android.app.Application
import io.buzypc.app.Data.BuildData.Store
import io.buzypc.app.Data.BuildData.Component
import io.buzypc.app.Data.BuildData.PC
import io.buzypc.app.Data.BuildData.Components.CPUComponent
import io.buzypc.app.Data.BuildData.Components.GPUComponent
import io.buzypc.app.Data.BuildData.Components.MotherboardComponent
import io.buzypc.app.Data.BuildData.Components.PSUComponent
import io.buzypc.app.Data.BuildData.Components.RAMComponent
import io.buzypc.app.Data.BuildData.Components.StorageDeviceComponent
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.UI.Utils.loadBuildList

class BuzyUserAppSession: Application() {
    var username: String = ""
    var theme: String = ""

    // information for new build -- will be passed to the AI
    var buildName: String = ""
    var buildBudget: Double = 0.0

    lateinit var buildList: ArrayList<PCBuild>
    lateinit var selectedBuildToSummarize: PCBuild
    lateinit var selectedBuildToViewChecklist: PCBuild

    lateinit var component: Component

    fun loadBuildList() {
        buildList = loadBuildList(this)
    }

    val pc = PC(
        motherboard = MotherboardComponent(
            "Z690",
            "LGA 1700",
            4,
            2,
            "Z690 Motherboard",
            "ASUS",
            14250.0,
            9.0f,
            listOf(
                Store(
                    "Lazada",
                    "https://laz-img-cdn.lazada.com.ph/g/p/xxxxxx/xxxxxx/xxxxxx/xxxxxx.jpg", // Placeholder image URL
                    "https://www.lazada.com.ph/products/asus-prime-z690-p-wifi-d4-lga-1700-atx-motherboard-i123456789.html",
                    "Z690 Motherboard",
                    14000.0..14500.0 // Example price range
                ),
                Store(
                    "Shopee",
                    "https://cf.shopee.ph/file/xxxxxx", // Placeholder image URL
                    "https://shopee.ph/ASUS-Prime-Z690-P-WIFI-D4-ATX-Motherboard-i987654321.html",
                    "Z690 Motherboard",
                    14100.0..14400.0 // Example price range
                ),
                Store(
                    "Amazon",
                    "https://m.media-amazon.com/images/I/xxxxxxxxx._AC_SL1500_.jpg", // Placeholder image URL
                    "https://www.amazon.ph/dp/B09PRVS1TS",
                    "Z690 Motherboard",
                    14200.0..14600.0 // Example price range
                )
            )
        ),
        cpu = CPUComponent(
            16,
            24,
            3.2f,
            32,
            "Intel Core i9-12900K",
            "Intel",
            31350.0,
            4.5f,
            listOf(
                Store(
                    "Lazada",
                    "https://laz-img-cdn.lazada.com.ph/g/p/xxxxxx/xxxxxx/xxxxxx/xxxxxx.jpg", // Placeholder image URL
                    "https://www.lazada.com.ph/products/intel-core-i9-12900kf-32-ghz-16-core-lga-1700-processor-i3511599892-s18092935700.html?c=&channelLpJumpArgs=&clickTrackInfo=query%253Aintel%252Bcore%252Bi9%252B12900k%252Bprocessor%253Bnid%253A3511599892%253Bsrc%253ALazadaMainSrp%253Brn%253Ab68ea62142555dd3a7294f619679f870%253Bregion%253Aph%253Bsku%253A3511599892_PH%253Bprice%253A33500%253Bclient%253Adesktop%253Bsupplier_id%253A100027113%253Bbiz_source%253Ah5_internal%253Bslot%253A0%253Butlog_bucket_id%253A470687%253Basc_category_id%253A5158%253Bitem_id%253A3511599892%253Bsku_id%253A18092935700%253Bshop_id%253A1778%253BtemplateInfo%253A107881_D_E%2523-1_A3_C%25231103_L%2523120442_A0%2523&freeshipping=1&fs_ab=2&fuse_fs=&lang=en&location=Metro%20Manila~Quezon%20City&price=3.35E%204&priceCompare=skuId%3A18092935700%3Bsource%3Alazada-search-voucher%3Bsn%3Ab68ea62142555dd3a7294f619679f870%3BoriginPrice%3A3350000%3BdisplayPrice%3A3350000%3BsinglePromotionId%3A-1%3BsingleToolCode%3A-1%3BvoucherPricePlugin%3A0%3Btimestamp%3A1744904270169&ratingscore=&request_id=b68ea62142555dd3a7294f619679f870&review=&sale=0&search=1&source=search&spm=a2o4l.searchlist.list.0&stock=1",
                    "Intel Core i9-12900K",
                    31000.0..31500.0 // Example price range
                ),
                Store(
                    "Shopee",
                    "https://cf.shopee.ph/file/xxxxxx", // Placeholder image URL
                    "https://shopee.ph/Intel-Core-i9-12900K-Processor-i876543210.html",
                    "Intel Core i9-12900K",
                    31200.0..31400.0 // Example price range
                ),
                Store(
                    "Amazon",
                    "https://m.media-amazon.com/images/I/xxxxxxxxx._AC_SL1500_.jpg", // Placeholder image URL
                    "https://www.amazon.ph/dp/B09HK68ZJY",
                    "Intel Core i9-12900K",
                    31300.0..31600.0 // Example price range
                )
            )
        ),
        gpu = GPUComponent(
            8,
            1500f,
            1750f,
            "GeForce RTX 3070",
            "Nvidia",
            34200.0,
            4.2f,
            listOf(
                Store(
                    "Lazada",
                    "https://laz-img-cdn.lazada.com.ph/g/p/xxxxxx/xxxxxx/xxxxxx/xxxxxx.jpg", // Placeholder image URL
                    "https://www.lazada.com.ph/products/nvidia-geforce-rtx-3070-8gb-gddr6-graphics-card-i192837465.html",
                    "GeForce RTX 3070",
                    34000.0..34500.0 // Example price range
                ),
                Store(
                    "Shopee",
                    "https://cf.shopee.ph/file/xxxxxx", // Placeholder image URL
                    "https://shopee.ph/NVIDIA-RTX-3070-8GB-GDDR6-Graphics-Card-i123450987.html",
                    "GeForce RTX 3070",
                    34100.0..34300.0 // Example price range
                ),
                Store(
                    "Amazon",
                    "https://m.media-amazon.com/images/I/xxxxxxxxx._AC_SL1500_.jpg", // Placeholder image URL
                    "https://www.amazon.ph/dp/B08KWPDX5L",
                    "GeForce RTX 3070",
                    34200.0..34600.0 // Example price range
                )
            )
        ),
        storageDevice = StorageDeviceComponent(
            "SSD",
            500,
            550,
            1000,
            "Samsung 970 Evo Plus",
            "Samsung",
            11400.0,
            4.7f,
            listOf(
                Store(
                    "Lazada",
                    "https://laz-img-cdn.lazada.com.ph/g/p/xxxxxx/xxxxxx/xxxxxx/xxxxxx.jpg", // Placeholder image URL
                    "https://www.lazada.com.ph/products/samsung-970-evo-plus-500gb-nvme-m2-ssd-i564738291.html",
                    "Samsung 970 Evo Plus",
                    11300.0..11600.0 // Example price range
                ),
                Store(
                    "Shopee",
                    "https://cf.shopee.ph/file/xxxxxx", // Placeholder image URL
                    "https://shopee.ph/Samsung-970-Evo-Plus-500GB-M.2-SSD-i109876543.html",
                    "Samsung 970 Evo Plus",
                    11400.0..11500.0 // Example price range
                ),
                Store(
                    "Amazon",
                    "https://m.media-amazon.com/images/I/xxxxxxxxx._AC_SL1500_.jpg", // Placeholder image URL
                    "https://www.amazon.ph/dp/B07MFZY2F2",
                    "Samsung 970 Evo Plus",
                    11450.0..11700.0 // Example price range
                )
            )
        ),
        ram = RAMComponent(
            32,
            3200,
            "DDR4",
            "Corsair Vengeance RGB Pro",
            "Corsair",
            8550.0,
            4.4f,
            listOf(
                Store(
                    "Lazada",
                    "https://laz-img-cdn.lazada.com.ph/g/p/xxxxxx/xxxxxx/xxxxxx/xxxxxx.jpg", // Placeholder image URL
                    "https://www.lazada.com.ph/products/corsair-vengeance-rgb-pro-32gb-2x16gb-ddr4-3200mhz-i837261945.html",
                    "Corsair Vengeance RGB Pro",
                    8400.0..8600.0 // Example price range
                ),
                Store(
                    "Shopee",
                    "https://cf.shopee.ph/file/xxxxxx", // Placeholder image URL
                    "https://shopee.ph/Corsair-Vengeance-RGB-Pro-32GB-2x16GB-DDR4-3200MHz-i234567890.html",
                    "Corsair Vengeance RGB Pro",
                    8500.0..8580.0 // Example price range
                ),
                Store(
                    "Amazon",
                    "https://m.media-amazon.com/images/I/xxxxxxxxx._AC_SL1500_.jpg", // Placeholder image URL
                    "https://www.amazon.ph/dp/B07GTG2T7X",
                    "Corsair Vengeance RGB Pro",
                    8550.0..8700.0 // Example price range
                )
            )
        ),
        psu = PSUComponent(
            850,
            true,
            "80+ Gold",
            "Corsair RM850x",
            "Corsair",
            6840.0,
            4.1f,
            listOf(
                Store(
                    "Lazada",
                    "https://laz-img-cdn.lazada.com.ph/g/p/xxxxxx/xxxxxx/xxxxxx/xxxxxx.jpg", // Placeholder image URL
                    "https://www.lazada.com.ph/products/corsair-rm850x-850w-80-gold-fully-modular-psu-i918273645.html",
                    "Corsair RM850x",
                    6700.0..6900.0 // Example price range
                ),
                Store(
                    "Shopee",
                    "https://cf.shopee.ph/file/xxxxxx", // Placeholder image URL
                    "https://shopee.ph/Corsair-RM850x-850W-80-Gold-Fully-Modular-PSU-i345678901.html",
                    "Corsair RM850x",
                    6800.0..6880.0 // Example price range
                ),
                Store(
                    "Amazon",
                    "https://m.media-amazon.com/images/I/xxxxxxxxx._AC_SL1500_.jpg", // Placeholder image URL
                    "https://www.amazon.ph/dp/B079H5WNXN",
                    "Corsair RM850x",
                    6850.0..7000.0 // Example price range
                )
            )
        )
    )
}
