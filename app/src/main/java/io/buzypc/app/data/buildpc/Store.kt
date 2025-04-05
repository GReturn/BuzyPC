package io.buzypc.app.data.buildpc

data class Store(
    val name: String,
    val imageUrl: String,
    val vendorSite: String,
    val associatedItem: String, // Name of the PC component
    val priceRange: Double
)
