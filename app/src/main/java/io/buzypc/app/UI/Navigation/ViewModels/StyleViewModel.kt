package io.buzypc.app.UI.Navigation.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StyleViewModel : ViewModel() {
    private val mutableTheme = MutableLiveData<Boolean>()
    val themeState: LiveData<Boolean> get() = mutableTheme

    fun setDarkMode(isDarkMode: Boolean) {
        mutableTheme.value = isDarkMode
    }
}