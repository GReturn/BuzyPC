package io.buzypc.app.data.appsession

import android.app.Application

class BuzyUserAppSession: Application() {
    var username: String = ""
    var theme: String = ""

    var buildBudget: String = ""
    var buildName: String = ""

    override fun onCreate() {
        super.onCreate()
    }
}
