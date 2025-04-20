package io.buzypc.app.UI.Utils

import android.content.Context
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.Data.SharedPrefManagers.BuzyUserBuildPrefManager
import io.buzypc.app.Data.SharedPrefManagers.SessionManager

fun loadBuildList(context: Context): ArrayList<PCBuild> {
    val buzyUserBuildPrefManager = BuzyUserBuildPrefManager(context)
    val sessionManager = SessionManager(context)
    return sessionManager.getUsername().let { buzyUserBuildPrefManager.getBuildList(it) }
}

fun saveBuildList(context: Context, list: ArrayList<PCBuild>) {
    val buildManager = BuzyUserBuildPrefManager(context)
    val sessionManager = SessionManager(context)
    buildManager.saveBuildList(sessionManager.getUsername(), list)
}