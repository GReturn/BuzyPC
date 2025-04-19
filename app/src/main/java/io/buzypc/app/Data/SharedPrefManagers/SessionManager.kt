package io.buzypc.app.Data.SharedPrefManagers

import android.content.Context

class SessionManager(
    context: Context
) {
    private val sharedPreferences = context.getSharedPreferences("buzy-session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"
    }

    fun createSession(username: String) {
        sharedPreferences.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USERNAME, username)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }
}