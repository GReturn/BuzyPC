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
    var currentComponentType: String? = null
    var componentName = ""
    var componentBrand = ""
    var componentPrice = 0.0
    var performanceScore = 0f
    var compatibilityScore = 0f
    var isBought = false
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
                    currentComponentType = currentTag
                    componentName = ""
                    componentBrand = ""
                    componentPrice = 0.0
                    performanceScore = 0f
                    compatibilityScore = 0f
                    isBought = false
                    stores = mutableListOf()
                } else if (currentTag == "Store") {
                    currentStore = Store("", "", 0.0..0.0)
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
                        "CompatibilityScore" -> compatibilityScore = text.toFloat()
                        "IsBought" -> isBought = text.toBoolean()

                        "Vendor" -> currentStore = currentStore?.copy(name = text)
                        "VendorSite" -> currentStore = currentStore?.copy(vendorSite = text)
                        "Min" -> currentStore = currentStore?.let {
                            val max = it.priceRange.endInclusive
                            it.copy(priceRange = text.toDouble()..max)
                        }
                        "Max" -> currentStore = currentStore?.let {
                            val min = it.priceRange.start
                            it.copy(priceRange = min..text.toDouble())
                        }
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
                        performanceScore, compatibilityScore, stores.toList()
                    ).apply { this.isBought = isBought }

                    "CPUComponent" -> cpu = CPUComponent(
                        componentName, componentBrand, componentPrice,
                        performanceScore, compatibilityScore, stores.toList()
                    ).apply { this.isBought = isBought }

                    "GPUComponent" -> gpu = GPUComponent(
                        componentName, componentBrand, componentPrice,
                        performanceScore, compatibilityScore, stores.toList()
                    ).apply { this.isBought = isBought }

                    "StorageDeviceComponent" -> storageDevice = StorageDeviceComponent(
                        componentName, componentBrand, componentPrice,
                        performanceScore, compatibilityScore, stores.toList()
                    ).apply { this.isBought = isBought }

                    "RAMComponent" -> ram = RAMComponent(
                        componentName, componentBrand, componentPrice,
                        performanceScore, compatibilityScore, stores.toList()
                    ).apply { this.isBought = isBought }

                    "PSUComponent" -> psu = PSUComponent(
                        componentName, componentBrand, componentPrice,
                        performanceScore, compatibilityScore, stores.toList()
                    ).apply { this.isBought = isBought }
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