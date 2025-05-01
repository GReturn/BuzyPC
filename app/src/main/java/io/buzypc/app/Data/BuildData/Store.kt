package io.buzypc.app.Data.BuildData

data class Store(
    val name: String,
    val vendorSite: String,
    val priceRange: ClosedFloatingPointRange<Double>
)
