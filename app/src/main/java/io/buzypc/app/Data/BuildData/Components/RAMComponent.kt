package io.buzypc.app.Data.BuildData.Components

import io.buzypc.app.Data.BuildData.Component
import io.buzypc.app.Data.BuildData.Store

class RAMComponent(
    name: String,
    brand: String,
    price: Double,
    performanceScore: Float,
    stores: List<Store>
) : Component(name, brand, price, performanceScore, stores)