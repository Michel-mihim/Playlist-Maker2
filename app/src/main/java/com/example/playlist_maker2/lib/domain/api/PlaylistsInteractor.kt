package com.example.playlist_maker2.lib.domain.api

import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun addPlaylist(
        playlist: Playlist,
        onGetResult: (Long) -> Unit
        )
    fun getPlaylists(): Flow<List<Playlist>>

}