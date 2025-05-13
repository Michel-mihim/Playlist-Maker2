package com.example.playlist_maker2.player.domain.models

import com.example.playlist_maker2.lib.domain.models.Playlist

sealed interface DBPlaylistsState {

    object Empty: DBPlaylistsState

    data class Content(val playlists: List<Playlist>): DBPlaylistsState

}