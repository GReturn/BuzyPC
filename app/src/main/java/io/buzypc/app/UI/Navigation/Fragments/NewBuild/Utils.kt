package io.buzypc.app.UI.Navigation.Fragments.NewBuild

import android.content.Context
import io.buzypc.app.UI.Utils.loadBuildList

fun generateUniqueBuildId(context: Context): Int {
    val list = loadBuildList(context)
    return (list.maxOfOrNull { it.id } ?: 0) + 1
}