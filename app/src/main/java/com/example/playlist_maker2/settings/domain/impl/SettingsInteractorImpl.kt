package com.example.playlist_maker2.settings.domain.impl

import com.example.playlist_maker2.settings.domain.api.SettingsInteractor
import com.example.playlist_maker2.settings.domain.api.SettingsRepository

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
):
    SettingsInteractor {
    override fun isThemeDark(): Boolean {
        return settingsRepository.isThemeInSettingsDark()
    }

    override fun writeThemeDark(setThemeDark: Boolean) {
        settingsRepository.writeThemeInSettingsDark(setThemeDark)
    }
}