package com.example.playlist_maker2.sharing.domain.api

import com.example.playlist_maker2.sharing.domain.models.EmailData

interface TextResourseGetter {
    fun getTermsLink(): String
    fun getShareAppLink(): String
    fun getSupportEmailData(): EmailData
}