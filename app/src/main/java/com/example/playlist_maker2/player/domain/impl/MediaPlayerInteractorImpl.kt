package com.example.playlist_maker2.player.domain.impl

import com.example.playlist_maker2.player.domain.api.MediaPlayerInteractor
import com.example.playlist_maker2.player.domain.api.MediaPlayerRepository

class MediaPlayerInteractorImpl(
    private val mediaPlayerRepository: MediaPlayerRepository
) : MediaPlayerInteractor {
    override fun prepare(
        url: String?,
        onPrepared: () -> Unit,
        onCompletion: () -> Unit
    ) {
        mediaPlayerRepository.prepare(url, onPrepared, onCompletion)
    }

    override fun start() {
        mediaPlayerRepository.start()
    }

    override fun pause() {
        mediaPlayerRepository.pause()
    }

    override fun release() {
        mediaPlayerRepository.release()
    }

    override fun timerUpdate(
        onTimerUpdated: (String) -> Unit
    ) {
        mediaPlayerRepository.timerUpdate(onTimerUpdated)
    }

}