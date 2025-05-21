package com.example.playlist_maker2.lib.ui

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker2.lib.domain.api.PlaylistSharingInteractor
import com.example.playlist_maker2.lib.domain.api.PlaylistsInteractor
import com.example.playlist_maker2.lib.domain.models.PlaylistEditState
import com.example.playlist_maker2.player.domain.api.TrackToPlaylistInteractor
import com.example.playlist_maker2.search.domain.api.GetPlayerIntentUseCase
import com.example.playlist_maker2.search.domain.models.Track
import com.example.playlist_maker2.utils.classes.SingleLiveEvent
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlaylistEditViewModel(
    private val trackToPlaylistInteractor: TrackToPlaylistInteractor,
    private val playlistInteractor: PlaylistsInteractor,
    private val getPlayerIntentUseCase: GetPlayerIntentUseCase,
    private val playlistSharingInteractor: PlaylistSharingInteractor
) : ViewModel() {

    private val playlistEditStateLiveData = MutableLiveData<PlaylistEditState>()
    fun observePlaylistEditState(): LiveData<PlaylistEditState> = playlistEditStateLiveData

    private val playerActivityIntentLiveData = SingleLiveEvent<Intent>()
    fun observePlayerActivityIntent(): LiveData<Intent> = playerActivityIntentLiveData

    private val shareActivityIntentLiveData = SingleLiveEvent<Intent>()
    fun observeShareActivityIntentLiveData(): LiveData<Intent> = shareActivityIntentLiveData

    fun showContent(playlistName: String) {
        viewModelScope.launch {
            trackToPlaylistInteractor.getState(playlistName).collect { state ->
                renderPlaylistEditState(state)
            }
        }
    }

    fun deletePlaylistTrack(trackId: String, playlistName: String) {
        runBlocking {
            trackToPlaylistInteractor.deleteTrack(
                trackId = trackId,
                playlistName = playlistName,
                onGetResult = { tracksCount, tracksDuration ->
                    viewModelScope.launch {
                        setPlaylistTracksCalculation(
                            playlistName,
                            tracksCount,
                            tracksDuration
                        )
                    }
                }
            )
        }

        showContent(playlistName)
    }

    fun sharePlaylist(text: String) {
        playlistSharingInteractor.sharePlaylist(
            text,
            onChooserReady = { intent ->
                startShareActivity(intent as Intent)
            })
    }

    suspend fun setPlaylistTracksCalculation(playlistName: String, tracksCount: Int, tracksDuration: Int) {
        playlistInteractor.setPlaylistTracksCalculation(
            playlistName = playlistName,
            playlistTracksCount = tracksCount,
            playlistTracksDuration = tracksDuration
        )
    }

    //PLAYER========================================================================================
    fun getPlayerIntent(track: Track) {
        getPlayerIntentUseCase.execute(
            track,
            onPlayerIntentReady = { intent ->
                startPlayerActivity(intent as Intent)
            }
        )
    }

    private fun startPlayerActivity(intent: Intent) {
        playerActivityIntentLiveData.postValue(intent)
    }

    //POSTING=======================================================================================
    private fun renderPlaylistEditState(state: PlaylistEditState) {
        playlistEditStateLiveData.postValue(state)
    }

    private fun startShareActivity(intent: Intent) {
        shareActivityIntentLiveData.postValue(intent)
    }

}