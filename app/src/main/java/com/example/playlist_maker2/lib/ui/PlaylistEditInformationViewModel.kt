package com.example.playlist_maker2.lib.ui

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker2.lib.domain.api.PlaylistsInteractor
import com.example.playlist_maker2.lib.domain.models.PlaylistInfoState
import com.example.playlist_maker2.player.domain.api.TrackToPlaylistInteractor
import com.example.playlist_maker2.utils.classes.SingleLiveEvent
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlaylistEditInformationViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val trackToPlaylistInteractor: TrackToPlaylistInteractor
) : ViewModel() {

    private val playlistInfoStateLiveData = MutableLiveData<PlaylistInfoState>()
    fun observePlaylistInfoState(): LiveData<PlaylistInfoState> = playlistInfoStateLiveData

    private val playlistDuplicateToastLiveData = SingleLiveEvent<String>()
    fun observePlaylistDuplicateToast(): LiveData<String> = playlistDuplicateToastLiveData

    fun showInformation(playlistName: String) {
        viewModelScope.launch {
            playlistsInteractor.readPlaylist(playlistName).collect { playlist ->
                val readPlaylistAbout = playlist.playlistAbout
                val readPlaylistName = playlist.playlistName

                renderPlaylistInfoState(PlaylistInfoState.Content(readPlaylistName, readPlaylistAbout))
            }
        }
    }

    fun setPlaylistInformation(
        oldPlaylistName: String,
        newPlaylistName: String,
        newPlaylistAbout: String,
        onPlaylistEdited: (Boolean) -> Unit
    ) {
        runBlocking {
            playlistsInteractor.checkPlaylistDuplicate(newPlaylistName).collect { isDuplicate ->
                if ((isDuplicate == false) || (oldPlaylistName == newPlaylistName)) {
                    playlistsInteractor.setPlaylistInformation(
                        oldPlaylistName = oldPlaylistName,
                        newPlaylistName = newPlaylistName,
                        newPlaylistAbout = newPlaylistAbout
                    )

                    trackToPlaylistInteractor.setPlaylistName(
                        oldPlaylistName = oldPlaylistName,
                        newPlaylistName = newPlaylistName
                    )

                    onPlaylistEdited.invoke(true)
                } else {
                    postPlaylistDuplicateNotification("Плейлист с указанным именем уже существует!")
                    onPlaylistEdited.invoke(false)
                }
            }
        }
    }

    private fun renderPlaylistInfoState(state: PlaylistInfoState) {
        playlistInfoStateLiveData.postValue(state)
    }

    private fun postPlaylistDuplicateNotification(message: String) {
        playlistDuplicateToastLiveData.postValue(message)
    }

}