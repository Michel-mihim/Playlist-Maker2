package com.example.playlist_maker2.lib.domain.impl

import com.example.playlist_maker2.lib.domain.api.PlaylistSharingInteractor
import com.example.playlist_maker2.lib.domain.api.PlaylistSharingRepository

class PlaylistSharingInteractorImpl(
    private val playlistSharingRepository: PlaylistSharingRepository
) : PlaylistSharingInteractor {

    override fun sharePlaylist(
        text: String,
        onChooserReady: (Any) -> Unit
    ) {
        playlistSharingRepository.sharePlaylist(text, onChooserReady)
    }
}