package io.buzypc.app.data.appsession

import android.app.Application

class BuzyUserAppSession: Application() {
    var username: String = ""
    var theme: String = ""

    override fun onCreate() {
        super.onCreate()
    }
}
