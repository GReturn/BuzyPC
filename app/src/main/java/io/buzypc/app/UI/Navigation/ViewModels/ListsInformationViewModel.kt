package io.buzypc.app.UI.Navigation.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.buzypc.app.Data.BuildData.PCBuild

// TODO (when we finally integrate and finish all the list views)

class ListsInformationViewModel : ViewModel() {
    private val mutableBuildListCount = MutableLiveData<Int>()
    private val mutableCheckListCount = MutableLiveData<Int>()

    val buildListCount: MutableLiveData<Int> get() = mutableBuildListCount
    val checkListCount: MutableLiveData<Int> get() = mutableCheckListCount

    fun incrementBuildCount() {
        mutableBuildListCount.value = (mutableBuildListCount.value ?: 0) + 1
    }
    fun incrementChecklistItemCount() {
        mutableCheckListCount.value = (mutableCheckListCount.value ?: 0) + 1
    }

    fun setBuildCount(count: Int) {
        mutableBuildListCount.value = count
    }

    fun setChecklistItemCount(count: Int) {
        mutableCheckListCount.value = count
    }
}