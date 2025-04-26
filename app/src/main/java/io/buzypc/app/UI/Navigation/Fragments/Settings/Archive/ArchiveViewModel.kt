package io.buzypc.app.UI.Navigation.Fragments.Settings.Archive

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.buzypc.app.Data.BuildData.PCBuild

class ArchiveViewModel: ViewModel() {
    private var mutableBuild = MutableLiveData<PCBuild>()

    val build: PCBuild? get() = mutableBuild.value
    fun setBuild(build:PCBuild){
        mutableBuild.value = build
    }
}