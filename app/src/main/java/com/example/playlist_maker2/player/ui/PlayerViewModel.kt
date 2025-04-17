package com.practicum.playlistmaker.player.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker2.player.domain.api.FavoriteTracksInteractor
import com.example.playlist_maker2.player.domain.api.MediaPlayerInteractor
import com.example.playlist_maker2.player.domain.models.PlayerActivityState
import com.example.playlist_maker2.player.domain.models.PlayerStatus
import com.example.playlist_maker2.search.domain.models.Track
import com.example.playlist_maker2.utils.classes.SingleLiveEvent
import com.example.playlist_maker2.utils.constants.Constants
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class PlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoriteTracksInteractor: FavoriteTracksInteractor
): ViewModel() {

    private var playerReadiness: Boolean = false

    private var playerStatus: PlayerStatus = PlayerStatus.STATE_DEFAULT

    private var currentProgress: String = ""

    private var likeButtonLiked: Boolean = false

    private val playerActivityCurrentStateLiveData = MutableLiveData<PlayerActivityState>()
    fun observePlayerActivityCurrentState(): LiveData<PlayerActivityState> = playerActivityCurrentStateLiveData

    private var timerJob: Job? = null

    //LIFE_CYCLE====================================================================================
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        mediaPlayerInteractor.release()
    }

    //PLAYBACK======================================================================================
    fun mediaPlayerPrepare(previewUrl: String?) {
        mediaPlayerInteractor.prepare(
            previewUrl,
            onPrepared = { ->
                playerReadiness = true
                playerStatus = PlayerStatus.STATE_PREPARED
                playerActivityPostState(
                    PlayerActivityState(
                        playerReadiness,
                        playerStatus,
                        "", //to exclude bug
                        likeButtonLiked
                    )
                )
            },
            onCompletion = { -> //окончание воспроизведения
                playerStatus = PlayerStatus.STATE_PREPARED
                currentProgress = ""//Constants.TRACK_IS_OVER_PROGRESS
                timerJob?.cancel()
                playerActivityPostState(
                    PlayerActivityState(
                        playerReadiness,
                        playerStatus,
                        currentProgress,
                        likeButtonLiked
                    )
                )
            }
        )
    }

    fun playbackControl() {
        when (playerStatus) {
            PlayerStatus.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerStatus.STATE_PREPARED -> {
                startPlayer()
            }
            PlayerStatus.STATE_PAUSED -> {
                startPlayer()
            }
            PlayerStatus.STATE_DEFAULT -> {}
        }
    }

    private fun startPlayer(){
        mediaPlayerInteractor.start()
        playerStatus = PlayerStatus.STATE_PLAYING
        playerActivityPostState(
            PlayerActivityState(
                playerReadiness,
                playerStatus,
                currentProgress,
                likeButtonLiked
            )
        )
        showProgress()
    }

    fun pausePlayer(){
        mediaPlayerInteractor.pause()
        playerStatus = PlayerStatus.STATE_PAUSED
        playerActivityPostState(
            PlayerActivityState(
                playerReadiness,
                playerStatus,
                currentProgress,
                likeButtonLiked
            )
        )
        timerJob?.cancel()
    }

    private fun showProgress(){
        mediaPlayerInteractor.timerUpdate(
            onTimerUpdated = { progress ->
                playerStatus = PlayerStatus.STATE_PLAYING
                currentProgress = progress
                playerActivityPostState(
                    PlayerActivityState(
                        playerReadiness,
                        playerStatus,
                        currentProgress,
                        likeButtonLiked
                    )
                )
            }
        )

        timerJob = viewModelScope.launch {
            delay(Constants.SHOW_PROGRESS_DELAY)
            showProgress()
        }
    }

    fun likeButtonSet(track: Track?) {
        if (track != null) {
            viewModelScope.launch {
                val isLiked: Deferred<Boolean> = async {
                    var result = false
                    favoriteTracksInteractor.getTracksById(track.trackId).collect { tracks ->
                        if (tracks.isEmpty()) {
                            result = false
                        } else {
                            result = true
                        }
                    }
                    return@async result
                }

                likeButtonLiked = isLiked.await()

                playerActivityPostState(
                    PlayerActivityState(
                        playerReadiness,
                        playerStatus,
                        currentProgress,
                        likeButtonLiked
                    )
                )
            }
        }
    }

    fun onFavoriteClicked(track: Track?) {
        if (track != null) {
            runBlocking {
                if (likeButtonLiked) {
                    favoriteTracksInteractor.deleteTrack(track)
                } else {
                    favoriteTracksInteractor.addTrack(track)
                }
            }

            likeButtonSet(track)
        }
    }

    //POSTING=======================================================================================

    private fun playerActivityPostState(playerActivityState: PlayerActivityState) {
        playerActivityCurrentStateLiveData.postValue(playerActivityState)
    }

}