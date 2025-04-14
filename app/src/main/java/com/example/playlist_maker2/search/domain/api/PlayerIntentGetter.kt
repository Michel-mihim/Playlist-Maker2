package com.example.playlist_maker2.search.domain.api

import com.example.playlist_maker2.search.domain.models.Track

interface PlayerIntentGetter {
    fun getPlayerIntent(
        track: Track,
        onPlayerIntentReady: (Any) -> Unit
    )
}