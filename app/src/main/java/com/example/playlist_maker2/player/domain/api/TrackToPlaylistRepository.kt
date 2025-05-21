package com.example.playlist_maker2.player.domain.api

import com.example.playlist_maker2.lib.domain.models.PlaylistEditState
import com.example.playlist_maker2.lib.domain.models.PlaylistTrack
import com.example.playlist_maker2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackToPlaylistRepository {

    suspend fun addTrack(
        playlistTrack: PlaylistTrack,
        onGetResult: (Long, Int, Int) -> Unit
    )

    suspend fun deleteTrack(
        trackId: String,
        playlistName: String,
        onGetResult: (Int, Int) -> Unit
    )

    fun getTracks(playlistName: String): Flow<List<PlaylistTrack>>

    fun getState(playlistName: String): Flow<PlaylistEditState>
}