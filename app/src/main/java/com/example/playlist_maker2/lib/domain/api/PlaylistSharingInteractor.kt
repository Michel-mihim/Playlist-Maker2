package com.example.playlist_maker2.lib.domain.api

interface PlaylistSharingInteractor {

    fun sharePlaylist(
        text: String,
        onChooserReady: (Any) -> Unit
    )
}