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
            playlistNewInteractor.addPlaylist(
                playlist,
                onGetResult = { result ->
                    var message = ""
                    if (result.toInt() == -1) {
                        message = "Плейлист уже существует"
                    } else {
                        message = "Плейлист "+playlist.playlistName+" создан"
                    }
                    showResult(message)
                }
                )
        }
    }

    //POSTING=======================================================================================
    private fun showResult(message: String) {
        playlistFragmentToastStateLiveData.postValue(message)
    }

}