package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker2.player.domain.api.MediaPlayerInteractor
import com.example.playlist_maker2.player.domain.models.PlayerActivityState
import com.example.playlist_maker2.player.domain.models.PlayerStatus
import com.example.playlist_maker2.utils.constants.Constants
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor
): ViewModel() {

    private var playerStatus: PlayerStatus = PlayerStatus.STATE_DEFAULT

    private var currentProgress: String = ""

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
                playerActivityPostState(
                    PlayerActivityState(
                        true,
                        PlayerStatus.STATE_PREPARED,
                        ""
                    )
                )
                playerStatus = PlayerStatus.STATE_PREPARED
            },
            onCompletion = { -> //окончание воспроизведения
                playerStatus = PlayerStatus.STATE_PREPARED
                timerJob?.cancel()
                playerActivityPostState(
                    PlayerActivityState(
                        true,
                        PlayerStatus.STATE_PREPARED,
                        Constants.TRACK_IS_OVER_PROGRESS
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
                true,
                PlayerStatus.STATE_PLAYING,
                currentProgress
            )
        )
        showProgress()
    }

    fun pausePlayer(){
        mediaPlayerInteractor.pause()
        playerStatus = PlayerStatus.STATE_PAUSED
        playerActivityPostState(
            PlayerActivityState(
                true,
                PlayerStatus.STATE_PAUSED,
                currentProgress
            )
        )
        timerJob?.cancel()
    }

    private fun showProgress(){
        mediaPlayerInteractor.timerUpdate(
            onTimerUpdated = { progress ->
                playerActivityPostState(
                    PlayerActivityState(
                        true,
                        PlayerStatus.STATE_PLAYING,
                        progress
                    )
                )
                currentProgress = progress
            }
        )

        timerJob = viewModelScope.launch {
            delay(Constants.SHOW_PROGRESS_DELAY)
            showProgress()
        }
    }

    //POSTING=======================================================================================

    private fun playerActivityPostState(playerActivityState: PlayerActivityState) {
        playerActivityCurrentStateLiveData.postValue(playerActivityState)
    }

}