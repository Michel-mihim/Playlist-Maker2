package com.example.playlist_maker2.lib.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker2.lib.domain.api.PlaylistsInteractor
import com.example.playlist_maker2.lib.domain.models.Playlist
import kotlinx.coroutines.launch

class PlaylistNewViewModel(
    val playlistNewInteractor: PlaylistsInteractor
) : ViewModel() {

    fun onAddPlaylistButtonClicked(playlist: Playlist) {
        viewModelScope.launch {
            playlistNewInteractor.addPlaylist(playlist)
        }
    }

}