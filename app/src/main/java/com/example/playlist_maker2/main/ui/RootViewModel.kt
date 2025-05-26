package com.example.playlist_maker2.main.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlist_maker2.lib.domain.models.PlaylistEditedResult

class RootViewModel : ViewModel() {

    private val playlistEditedResult = MutableLiveData<PlaylistEditedResult>()
    fun observePlaylistEditedResult(): LiveData<PlaylistEditedResult> = playlistEditedResult

    fun setPlaylistEditedResult(isEdited: Boolean, newName: String) {
        playlistEditedResult.postValue(PlaylistEditedResult(isEdited, newName))
    }

}