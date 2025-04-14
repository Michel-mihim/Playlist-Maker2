package com.example.playlist_maker2.sharing.domain.api

import com.example.playlist_maker2.sharing.domain.models.EmailData

interface ExternalNavigator {
    fun shareLink(link: String, onChooserReady: (Any) -> Unit)
    fun openLink(link: String, onTermsIntentReady: (Any) -> Unit)
    fun openEmail(email: EmailData, onSupportEmailIntentReady: (Any) -> Unit)
}