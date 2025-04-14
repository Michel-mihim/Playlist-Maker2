package com.example.playlist_maker2.sharing.domain.api

interface SharingInteractor {
    fun shareApp(onChooserReady: (Any) -> Unit)
    fun openTerms(onTermsIntentReady: (Any) -> Unit)
    fun openSupport(onSupportEmailIntentReady: (Any) -> Unit)
}