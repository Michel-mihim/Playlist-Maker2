package com.example.playlist_maker2.settings.ui

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist_maker2.App
import com.example.playlist_maker2.settings.domain.api.SettingsInteractor
import com.example.playlist_maker2.sharing.domain.api.SharingInteractor
import com.example.playlist_maker2.utils.classes.SingleLiveEvent

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor,
    private val app_link: Application
): ViewModel() {

    //==============================================================================================
    //SETTINGS======================================================================================
    private val settingsActivityDarkThemeLiveData = MutableLiveData<Boolean>()
    fun observeSettingsActivityTheme(): LiveData<Boolean> = settingsActivityDarkThemeLiveData

    //POSTING=======================================================================================
    private fun themeSwitcherIsDarkSetter(isDark: Boolean) {
        settingsActivityDarkThemeLiveData.postValue(isDark)
    }
    //==============================================================================================

    init {
        themeSwitcherIsDarkSetter(settingsInteractor.isThemeDark())
    }

    fun switchTheme(checked: Boolean) {
        (app_link as App).switchTheme(checked)

        settingsInteractor.writeThemeDark(checked)
        themeSwitcherIsDarkSetter(checked)
    }

    //==============================================================================================
    //SHARING=======================================================================================

    private val shareActivityIntentLiveData = SingleLiveEvent<Intent>()
    fun observeShareActivityIntentLiveData(): LiveData<Intent> = shareActivityIntentLiveData

    private val supportEmailActivityIntentLiveData = SingleLiveEvent<Intent>()
    fun observeSupportEmailActivityIntentLiveData(): LiveData<Intent> = supportEmailActivityIntentLiveData

    private val termsIntentLiveData = SingleLiveEvent<Intent>()
    fun observeTermsIntentLiveData(): LiveData<Intent> = termsIntentLiveData

    fun shareApp() {
        sharingInteractor.shareApp(
            onChooserReady = { intent ->
                startShareActivity(intent as Intent)
            }
        )
    }

    fun openSupport() {
        sharingInteractor.openSupport(
            onSupportEmailIntentReady = { intent ->
                startSupportEmailActivity(intent as Intent)
            }
        )
    }

    fun openTerms() {
        sharingInteractor.openTerms(
            onTermsIntentReady = { intent ->
                startTermsIntentActivity(intent as Intent)
            }
        )
    }

    //POSTING=======================================================================================
    private fun startShareActivity(intent: Intent) {
        shareActivityIntentLiveData.postValue(intent)
    }

    private fun startSupportEmailActivity(intent: Intent) {
        supportEmailActivityIntentLiveData.postValue(intent)
    }

    private fun startTermsIntentActivity(intent: Intent) {
        termsIntentLiveData.postValue(intent)
    }

}