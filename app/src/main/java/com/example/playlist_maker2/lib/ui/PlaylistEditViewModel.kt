package com.example.playlist_maker2.lib.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker2.lib.domain.api.PlaylistsInteractor
import com.example.playlist_maker2.lib.domain.models.PlaylistEditState
import com.example.playlist_maker2.lib.domain.models.PlaylistTrack
import com.example.playlist_maker2.player.domain.api.TrackToPlaylistInteractor
import com.example.playlist_maker2.player.domain.models.DBPlaylistsState
import com.example.playlist_maker2.search.domain.models.Track
import kotlinx.coroutines.launch

class PlaylistEditViewModel(
    private val trackToPlaylistInteractor: TrackToPlaylistInteractor
) : ViewModel() {

    private val playlistEditStateLiveData = MutableLiveData<PlaylistEditState>()
    fun observePlaylistEditState(): LiveData<PlaylistEditState> = playlistEditStateLiveData

    fun showContent(playlistName: String) {
        viewModelScope.launch {
            trackToPlaylistInteractor.getState(playlistName).collect { state ->
                renderPlaylistEditState(state)
            }
        }
    }

    //POSTING=======================================================================================
    private fun renderPlaylistEditState(state: PlaylistEditState) {
        playlistEditStateLiveData.postValue(state)
    }

}