package com.example.playlist_maker2.lib.ui

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker2.lib.domain.api.PlaylistsInteractor
import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.search.domain.models.SearchActivityState
import com.example.playlist_maker2.utils.classes.SingleLiveEvent
import kotlinx.coroutines.launch

class PlaylistNewViewModel(
    val playlistNewInteractor: PlaylistsInteractor
) : ViewModel() {

    private val playlistFragmentToastStateLiveData = SingleLiveEvent<String>()
    fun observePlaylistFragmentToastState(): LiveData<String> = playlistFragmentToastStateLiveData

    fun onAddPlaylistButtonClicked(playlist: Playlist) {
        viewModelScope.launch {
            playlistNewInteractor.addPlaylist(playlist)

            showToastState("Плейлист "+playlist.playlistName+" создан")
            Log.d("wtf", "message")
        }
    }

    //POSTING=======================================================================================
    private fun showToastState(message: String) {
        Log.d("wtf", message)
        playlistFragmentToastStateLiveData.postValue(message)
    }

}