package com.example.playlist_maker2.player.domain.api

import com.example.playlist_maker2.lib.domain.models.PlaylistTrack
import kotlinx.coroutines.flow.Flow

interface TrackToPlaylistRepository {

    suspend fun addTrack(
        playlistTrack: PlaylistTrack,
        onGetResult: (Long, Int, Int) -> Unit
    )

}