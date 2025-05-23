package com.example.playlist_maker2.lib.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker2.lib.domain.api.PlaylistsInteractor
import com.example.playlist_maker2.lib.domain.models.PlaylistInfoState
import com.example.playlist_maker2.player.domain.api.TrackToPlaylistInteractor
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlaylistEditInformationViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val trackToPlaylistInteractor: TrackToPlaylistInteractor
) : ViewModel() {

    private val playlistInfoStateLiveData = MutableLiveData<PlaylistInfoState>()
    fun observePlaylistInfoState(): LiveData<PlaylistInfoState> = playlistInfoStateLiveData

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
            playlistsInteractor.setPlaylistInformation(
                oldPlaylistName = oldPlaylistName,
                newPlaylistName = newPlaylistName,
                newPlaylistAbout = newPlaylistAbout
            )

            trackToPlaylistInteractor.setPlaylistName(
                oldPlaylistName = oldPlaylistName,
                newPlaylistName = newPlaylistName
            )
        }

        onPlaylistEdited.invoke(true)
    }

    private fun renderPlaylistInfoState(state: PlaylistInfoState) {
        playlistInfoStateLiveData.postValue(state)
    }
}