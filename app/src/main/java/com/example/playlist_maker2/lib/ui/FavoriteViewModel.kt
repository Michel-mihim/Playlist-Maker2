package com.example.playlist_maker2.lib.ui

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker2.player.domain.api.FavoriteTracksInteractor
import com.example.playlist_maker2.player.domain.models.DBActivityState
import com.example.playlist_maker2.search.domain.api.GetPlayerIntentUseCase
import com.example.playlist_maker2.search.domain.models.SearchActivityNavigationState
import com.example.playlist_maker2.search.domain.models.SearchActivityState
import com.example.playlist_maker2.search.domain.models.Track
import com.example.playlist_maker2.utils.classes.SingleLiveEvent
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor,
    private val getPlayerIntentUseCase: GetPlayerIntentUseCase
) : ViewModel() {

    private var favoriteTracks = ArrayList<Track>()

    private val favoriteTracksStateLiveData = MutableLiveData<DBActivityState>()
    fun observeFavoriteTracksState(): LiveData<DBActivityState> = favoriteTracksStateLiveData

    private val playerActivityIntentLiveData = SingleLiveEvent<Intent>()
    fun observePlayerActivityIntent(): LiveData<Intent> = playerActivityIntentLiveData

    fun showFavorite() {
        favoriteTracks.clear()

        viewModelScope.launch {
            favoriteTracksInteractor.getTracks().collect { tracks ->
                favoriteTracks.addAll(tracks)

                if (favoriteTracks.isNotEmpty()) {
                    renderState(DBActivityState.Content(favoriteTracks))
                } else renderState(DBActivityState.Empty)
            }

        }
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

    //POSTING=======================================================================================
    private fun startPlayerActivity(intent: Intent) {
        playerActivityIntentLiveData.postValue(intent)
    }

    private fun renderState(state: DBActivityState) {
        favoriteTracksStateLiveData.postValue(state)
    }
}