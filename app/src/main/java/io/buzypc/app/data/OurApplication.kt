package io.buzypc.app.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import io.buzypc.app.data.user.BuzyUser

class OurApplication: Application() {

    // Global (non-user-specific) data
    var buildBudget: String = ""
    var buildName: String = ""
    var username: String = ""
    var theme: String = ""
}
