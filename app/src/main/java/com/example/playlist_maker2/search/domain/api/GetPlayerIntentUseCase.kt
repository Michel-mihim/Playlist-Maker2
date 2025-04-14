package com.example.playlist_maker2.search.domain.api

import com.example.playlist_maker2.search.domain.models.Track

class GetPlayerIntentUseCase(private val playerIntentGetter: PlayerIntentGetter) {
    fun execute(
        track: Track,
        onPlayerIntentReady: (Any) -> Unit
    ) {
        playerIntentGetter.getPlayerIntent(track, onPlayerIntentReady)
    }
}