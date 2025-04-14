package com.example.playlist_maker2.settings.domain.api


interface SettingsInteractor {
    fun isThemeDark(): Boolean
    fun writeThemeDark(setThemeDark: Boolean)
}