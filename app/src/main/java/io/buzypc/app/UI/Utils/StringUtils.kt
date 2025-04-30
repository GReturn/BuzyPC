package io.buzypc.app.UI.Utils

import java.text.NumberFormat
import java.util.Locale

fun formatDecimalPriceToPesoCurrencyString(price: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
    return format.format(price)
}