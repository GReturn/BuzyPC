package io.buzypc.app.data

import android.content.Context

class BuzyUserSettings(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("buzy-user-settings", Context.MODE_PRIVATE)

    fun getTheme() : String? = sharedPreferences.getString("theme", null)

    fun setTheme(newTheme: String) = sharedPreferences.edit()
        .putString("theme", newTheme).apply()
}