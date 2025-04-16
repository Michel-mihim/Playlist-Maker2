package com.example.playlist_maker2.lib.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlist_maker2.player.domain.api.FavoriteTracksInteractor
import com.example.playlist_maker2.search.domain.models.SearchActivityNavigationState
import com.example.playlist_maker2.search.domain.models.SearchActivityState
import com.example.playlist_maker2.search.domain.models.Track
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {

    private var favoriteTracks = ArrayList<Track>()

    private val favoriteTracksLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeFavoriteTracks(): LiveData<ArrayList<Track>> = favoriteTracksLiveData

    fun showFavorite() {
        favoriteTracks.clear()

        viewModelScope.launch {
            favoriteTracksInteractor.getTracks().collect { tracks ->
                favoriteTracks.addAll(tracks)

                if (favoriteTracks.isNotEmpty()) {
                    renderTracks(favoriteTracks)
                } else renderTracks(ArrayList())
            }

        }
    }

    //POSTING=======================================================================================
    private fun renderTracks(tracks: ArrayList<Track>) {
        favoriteTracksLiveData.postValue(tracks)
    }
}