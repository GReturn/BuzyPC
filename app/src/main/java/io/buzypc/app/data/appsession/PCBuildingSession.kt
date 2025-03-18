package io.buzypc.app.data.appsession

import android.app.Application

class PCBuildingSession : Application() {
    var buildBudget: String = ""
    var buildName: String = ""

    override fun onCreate() {
        super.onCreate()
    }

}