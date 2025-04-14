package com.example.playlist_maker2.sharing.data.impl

import android.content.Intent
import android.net.Uri
import com.example.playlist_maker2.sharing.domain.api.ExternalNavigator
import com.example.playlist_maker2.sharing.domain.models.EmailData

class ExternalNavigatorImpl(
    private val shareLinkIntent: Intent,
    private val chooser: Intent,
    private val supportEmailIntent: Intent,
    private val termsIntent: Intent
) : ExternalNavigator {

    override fun shareLink(
        link: String,
        onChooserReady: ((Any) -> Unit)
    ) {
        shareLinkIntent.setType("text/plain")
        shareLinkIntent.putExtra(Intent.EXTRA_SUBJECT, "Полезная ссылка")
        shareLinkIntent.putExtra(Intent.EXTRA_TEXT, link)
        onChooserReady.invoke(chooser)
    }

    override fun openLink(
        link: String,
        onTermsIntentReady: ((Any) -> Unit)
    ) {
        termsIntent.data = Uri.parse(link)
        onTermsIntentReady.invoke(termsIntent)
    }

    override fun openEmail(
        email: EmailData,
        onSupportEmailIntentReady: ((Any) -> Unit)
    ) {
        supportEmailIntent.data = Uri.parse("mailto:")
        supportEmailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email.email))
        supportEmailIntent.putExtra(Intent.EXTRA_SUBJECT, email.subject)
        supportEmailIntent.putExtra(Intent.EXTRA_TEXT, email.text)
        onSupportEmailIntentReady.invoke(supportEmailIntent)
    }

}