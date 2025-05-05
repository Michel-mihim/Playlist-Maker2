package com.example.playlist_maker2.lib.domain.api

import com.example.playlist_maker2.lib.domain.models.Playlist

interface PlaylistsRepository {

    suspend fun addPlaylist(playlist: Playlist)
}