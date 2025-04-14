package com.example.playlist_maker2.sharing.data.impl

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.example.playlist_maker2.R
import com.example.playlist_maker2.sharing.domain.api.TextResourseGetter
import com.example.playlist_maker2.sharing.domain.models.EmailData

class TextResourseGetterImpl(
    val context: Context
): TextResourseGetter {
    override fun getTermsLink(): String {
        return getString(context, R.string.license_url)
    }

    override fun getShareAppLink(): String {
        return getString(context, R.string.share_message)
    }

    override fun getSupportEmailData(): EmailData {
        return EmailData(
            getString(context, R.string.student_email),
            getString(context, R.string.letter_text_subject),
            getString(context, R.string.letter_text)
        )
    }
}