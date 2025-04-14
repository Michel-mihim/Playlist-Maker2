package com.example.playlist_maker2.search.domain.models

sealed interface SearchActivityState {

    object Loading: SearchActivityState

    object Default: SearchActivityState

    object Empty: SearchActivityState

    object Error: SearchActivityState

    data class History(val tracks: List<Track>): SearchActivityState

    data class Content(val tracks: List<Track>): SearchActivityState

}