package io.buzypc.app.UI.Utils

import java.text.DecimalFormat

fun formatDecimalPriceToString(price: Double): String {
    return DecimalFormat("#,###.00").format(price)
}