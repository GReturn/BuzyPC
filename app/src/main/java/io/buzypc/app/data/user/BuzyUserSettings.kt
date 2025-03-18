package io.buzypc.app.data.user

import android.content.Context

class BuzyUserSettings(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("buzy-user-settings", Context.MODE_PRIVATE)

    fun getTheme(): String? = sharedPreferences.getString("theme", null)

    fun setLastUser(username: String?) {
        // Store the username under the key "lastUser"
        sharedPreferences.edit().putString("lastUser", username).apply()
    }
    fun getLastUser(): String? = sharedPreferences.getString("lastUser", null)


    fun setTheme(newTheme: String) = sharedPreferences.edit()
        .putString("theme", newTheme).apply()
}
