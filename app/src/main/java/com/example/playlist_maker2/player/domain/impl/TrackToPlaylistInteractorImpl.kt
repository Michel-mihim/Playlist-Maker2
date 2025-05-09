package com.example.playlist_maker2.player.domain.impl

import com.example.playlist_maker2.lib.domain.models.PlaylistTrack
import com.example.playlist_maker2.player.domain.api.TrackToPlaylistInteractor
import com.example.playlist_maker2.player.domain.api.TrackToPlaylistRepository
import kotlinx.coroutines.flow.Flow

class TrackToPlaylistInteractorImpl(
    private val trackToPlaylistRepository: TrackToPlaylistRepository
) : TrackToPlaylistInteractor {

    override suspend fun addTrack(
        playlistTrack: PlaylistTrack,
        onGetResult: (Long) -> Unit
    ) {
        trackToPlaylistRepository.addTrack(
            playlistTrack,
            onGetResult
        )
    }

    override fun getTracksCount(playlistName: String): Flow<Long> {
        return trackToPlaylistRepository.getTracksCount(playlistName)
    }
}