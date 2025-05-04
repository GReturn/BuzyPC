package io.buzypc.app.Data.SharedPrefManagers

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.buzypc.app.Data.BuildData.PCBuild
import io.buzypc.app.Data.BuildData.Utils.JsonAdapters.ClosedFloatingPointRangeAdapter

class BuzyUserBuildPrefManager(
    context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("pc_build_prefs", Context.MODE_PRIVATE)
    private val gson = GsonBuilder()
        .registerTypeAdapter(ClosedFloatingPointRange::class.java, ClosedFloatingPointRangeAdapter())
        .create()

    // Always call this with a valid username
    fun getBuildList(username: String): ArrayList<PCBuild> {
        val json = prefs.getString(getKey(username), null)
        return if (json != null) {
            val type = object : TypeToken<ArrayList<PCBuild>>() {}.type
            gson.fromJson(json, type)
        } else {
            ArrayList()
        }
    }

    fun saveBuildList(username: String, list: ArrayList<PCBuild>) {
        val json = gson.toJson(list)
        prefs.edit().putString(getKey(username), json).apply()
    }

    fun addBuild(username: String, build: PCBuild) {
        val list = getBuildList(username)
        list.add(build)
        saveBuildList(username, list)
    }

    fun removeBuild(username: String, buildId: Int) {
        val list = getBuildList(username)
        list.removeAll { it.id == buildId }
        saveBuildList(username, list)
    }

    fun getBuildById(username: String, buildId: Int): PCBuild? {
        return getBuildList(username).find { it.id == buildId }
    }

    fun renameBuild(username: String, renamedBuild: PCBuild){
        val list = getBuildList(username)
        list.replaceAll { existing ->
            if (existing.id == renamedBuild.id) renamedBuild else existing
        }
        saveBuildList(username, list)
    }

    private fun getKey(username: String): String = "pc_build_list_$username"
}