package com.example.playlist_maker2.settings.domain.api

interface SettingsRepository {
    fun isThemeInSettingsDark(): Boolean
    fun writeThemeInSettingsDark(setDark: Boolean)
}