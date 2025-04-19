package io.buzypc.app.Data.SharedPrefManagers

import android.content.Context
import android.content.SharedPreferences

class BuzyUserBuildPrefManager(context: Context) {
    private lateinit var sharedPreferences: SharedPreferences

    init {
        loadUser()
    }
//    private val prefs: SharedPreferences = context.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
}