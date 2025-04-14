package com.example.playlist_maker2.sharing.domain.impl

import com.example.playlist_maker2.sharing.domain.api.ExternalNavigator
import com.example.playlist_maker2.sharing.domain.api.SharingInteractor
import com.example.playlist_maker2.sharing.domain.api.TextResourseGetter
import com.example.playlist_maker2.sharing.domain.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val textResourseGetter: TextResourseGetter
) : SharingInteractor {
    override fun shareApp(onChooserReady: (Any) -> Unit) {
        externalNavigator.shareLink(getShareAppLink(), onChooserReady)
    }

    override fun openTerms(onTermsIntentReady: (Any) -> Unit) {
        externalNavigator.openLink(getTermsLink(), onTermsIntentReady)
    }

    override fun openSupport(onSupportEmailIntentReady: (Any) -> Unit) {
        externalNavigator.openEmail(getSupportEmailData(), onSupportEmailIntentReady)
    }

    private fun getShareAppLink(): String {
        return textResourseGetter.getShareAppLink()
    }

    private fun getTermsLink(): String {
        return textResourseGetter.getTermsLink()
    }

    private fun getSupportEmailData(): EmailData {
        return textResourseGetter.getSupportEmailData()
    }

}