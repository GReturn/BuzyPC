package io.buzypc.app.Data.SharedPrefManagers

import android.content.Context
import io.buzypc.app.Data.AppSession.BuzyUserAppSession

class BuzyUserSettingsManager(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("buzy-user-settings_${BuzyUserAppSession().username}", Context.MODE_PRIVATE)

    fun getTheme(): String? = sharedPreferences.getString("theme", null)

    fun setTheme(newTheme: String) = sharedPreferences.edit()
        .putString("theme", newTheme).apply()
}
