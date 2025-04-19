package io.buzypc.app.UI.Navigation.Fragments.BuildTracker.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.buzypc.app.Data.BuildData.PCBuild

class BuildTrackerViewModel : ViewModel() {
    private val focusedPCBuild = MutableLiveData<PCBuild>()
    val currentBuild: MutableLiveData<PCBuild> get() = focusedPCBuild

    fun setFocusedPCBuild(build: PCBuild) {
        focusedPCBuild.value = build
    }
}