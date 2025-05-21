package com.example.playlist_maker2.player.domain.impl

import android.util.Log
import com.example.playlist_maker2.lib.domain.models.PlaylistEditState
import com.example.playlist_maker2.lib.domain.models.PlaylistTrack
import com.example.playlist_maker2.player.domain.api.TrackToPlaylistInteractor
import com.example.playlist_maker2.player.domain.api.TrackToPlaylistRepository
import com.example.playlist_maker2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class TrackToPlaylistInteractorImpl(
    private val trackToPlaylistRepository: TrackToPlaylistRepository
) : TrackToPlaylistInteractor {

    override suspend fun addTrack(
        playlistTrack: PlaylistTrack,
        onGetResult: (Long, Int, Int) -> Unit
    ) {
        trackToPlaylistRepository.addTrack(
            playlistTrack,
            onGetResult
        )
    }

    override suspend fun deleteTrack(
        trackId: String,
        playlistName: String,
        onGetResult: (Int, Int) -> Unit
    ) {
        trackToPlaylistRepository.deleteTrack(
            trackId,
            playlistName,
            onGetResult
        )
    }

    override fun getTracks(playlistName: String): Flow<List<PlaylistTrack>> {
        return trackToPlaylistRepository.getTracks(playlistName)
    }

    override fun getState(playlistName: String): Flow<PlaylistEditState> {
        return trackToPlaylistRepository.getState(playlistName)
    }

}