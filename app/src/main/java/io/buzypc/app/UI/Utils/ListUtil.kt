package io.buzypc.app.UI.Utils

import android.content.Context
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.Data.SharedPrefManagers.BuzyUserBuildPrefManager
import io.buzypc.app.Data.SharedPrefManagers.SessionManager

fun saveBuildList(context: Context, list: ArrayList<PCBuild>) {
    val buildManager = BuzyUserBuildPrefManager(context)
    val sessionManager = SessionManager(context)
    buildManager.saveBuildList(sessionManager.getUsername(), list)
}