package com.example.playlist_maker2.lib.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker2.lib.domain.api.PlaylistsInteractor
import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.player.domain.models.DBPlaylistsState
import kotlinx.coroutines.launch

class PlaylistViewModel(
    val playlistNewInteractor: PlaylistsInteractor
) : ViewModel() {

    private var playlists = ArrayList<Playlist>()

    private val playlistsStateLiveData = MutableLiveData<DBPlaylistsState>()
    fun observePlaylistsState(): LiveData<DBPlaylistsState> = playlistsStateLiveData

    fun showPlaylists() {
        playlists.clear()

        viewModelScope.launch {
            playlistNewInteractor.getPlaylists().collect { playlistsFromDb ->
                playlists.addAll(playlistsFromDb)

                if (playlists.isNotEmpty()) {
                    renderState(DBPlaylistsState.Content(playlists))
                } else renderState(DBPlaylistsState.Empty)
            }
        }
    }

    //POSTING=======================================================================================
    private fun renderState(state: DBPlaylistsState) {
        playlistsStateLiveData.postValue(state)
    }

}