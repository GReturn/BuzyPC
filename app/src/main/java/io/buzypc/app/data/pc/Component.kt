package io.buzypc.app.data.pc

import io.buzypc.app.data.buildpc.Store

open class Component(
    val name: String,
    val brand: String,
    val price: Double,
    val performanceScore: Float,
    val stores: List<Store>
){
}

