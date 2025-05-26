package com.example.playlist_maker2.lib.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker2.lib.domain.api.PlaylistsInteractor
import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.utils.classes.SingleLiveEvent
import com.example.playlist_maker2.utils.constants.Constants
import kotlinx.coroutines.launch

class PlaylistNewViewModel(
    val playlistInteractor: PlaylistsInteractor
) : ViewModel() {

    private val playlistFragmentToastStateLiveData = SingleLiveEvent<String>()
    fun observePlaylistFragmentToastState(): LiveData<String> = playlistFragmentToastStateLiveData

    fun onAddPlaylistButtonClicked(
        playlist: Playlist,
        onPlaylistAddResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            playlistInteractor.addPlaylist(
                playlist,
                onGetResult = { result ->
                    var message = ""
                    if (result.toInt() == -1) {
                        message = Constants.PLAYLIST_DUPLICATE_ERROR
                        onPlaylistAddResult.invoke(false)
                    } else {
                        message = "Плейлист "+playlist.playlistName+" создан"
                        onPlaylistAddResult.invoke(true)
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