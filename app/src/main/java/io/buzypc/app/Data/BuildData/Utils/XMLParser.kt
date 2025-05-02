package io.buzypc.app.Data.BuildData.Utils

import io.buzypc.app.Data.BuildData.Components.CPUComponent
import io.buzypc.app.Data.BuildData.Components.GPUComponent
import io.buzypc.app.Data.BuildData.Components.MotherboardComponent
import io.buzypc.app.Data.BuildData.Components.PSUComponent
import io.buzypc.app.Data.BuildData.Components.RAMComponent
import io.buzypc.app.Data.BuildData.Components.StorageDeviceComponent
import io.buzypc.app.Data.BuildData.PC
import io.buzypc.app.Data.BuildData.Store
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader

fun parsePCBuild(xml: String): PC {
    val factory = XmlPullParserFactory.newInstance()
    factory.isNamespaceAware = true
    val parser = factory.newPullParser()
    parser.setInput(StringReader(xml))

    var currentTag: String? = null
    var componentName = ""
    var componentBrand = ""
    var componentPrice = 0.0
    var performanceScore = 0f
    var stores = mutableListOf<Store>()
    var currentStore: Store? = null

    lateinit var motherboard: MotherboardComponent
    lateinit var cpu: CPUComponent
    lateinit var gpu: GPUComponent
    lateinit var storageDevice: StorageDeviceComponent
    lateinit var ram: RAMComponent
    lateinit var psu: PSUComponent

    var eventType = parser.eventType
    while (eventType != XmlPullParser.END_DOCUMENT) {
        when (eventType) {
            XmlPullParser.START_TAG -> {
                currentTag = parser.name
                if (currentTag!!.endsWith("Component")) {
                    componentName = ""
                    componentBrand = ""
                    componentPrice = 0.0
                    performanceScore = 0f
                    stores = mutableListOf()
                } else if (currentTag == "Store") {
                    currentStore = Store("", "")
                }
            }

            XmlPullParser.TEXT -> {
                val text = parser.text.trim()
                if (text.isNotEmpty()) {
                    when (currentTag) {
                        "Name" -> componentName = text
                        "Brand" -> componentBrand = text
                        "Price" -> componentPrice = text.toDouble()
                        "PerformanceScore" -> performanceScore = text.toFloat()

                        "Vendor" -> currentStore = currentStore?.copy(name = text)
                        "VendorSite" -> currentStore = currentStore?.copy(vendorSite = text)
                    }
                }
            }

            XmlPullParser.END_TAG -> {
                when (parser.name) {
                    "Store" -> {
                        currentStore?.let { stores.add(it) }
                    }

                    "MotherboardComponent" -> motherboard = MotherboardComponent(
                        componentName, componentBrand, componentPrice,
                        performanceScore, stores.toList()
                    ).apply { this.isBought = false }

                    "CPUComponent" -> cpu = CPUComponent(
                        componentName, componentBrand, componentPrice,
                        performanceScore, stores.toList()
                    ).apply { this.isBought = false }

                    "GPUComponent" -> gpu = GPUComponent(
                        componentName, componentBrand, componentPrice,
                        performanceScore, stores.toList()
                    ).apply { this.isBought = false }

                    "StorageDeviceComponent" -> storageDevice = StorageDeviceComponent(
                        componentName, componentBrand, componentPrice,
                        performanceScore, stores.toList()
                    ).apply { this.isBought = false }

                    "RAMComponent" -> ram = RAMComponent(
                        componentName, componentBrand, componentPrice,
                        performanceScore, stores.toList()
                    ).apply { this.isBought = false }

                    "PSUComponent" -> psu = PSUComponent(
                        componentName, componentBrand, componentPrice,
                        performanceScore, stores.toList()
                    ).apply { this.isBought = false }
                }
            }
        }
        eventType = parser.next()
    }

    return PC(
        motherboard = motherboard,
        cpu = cpu,
        gpu = gpu,
        storageDevice = storageDevice,
        ram = ram,
        psu = psu
    )
}