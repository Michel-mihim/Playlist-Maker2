package com.example.playlist_maker2.player.domain.api

interface MediaPlayerInteractor {
    fun prepare(
        url: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    )
    fun start()
    fun pause()
    fun release()
    fun timerUpdate(
        onTimerUpdated: (String) -> Unit
    )
}