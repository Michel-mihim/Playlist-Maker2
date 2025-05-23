package com.example.playlist_maker2.lib.domain.models

sealed interface PlaylistEditState {

    data class Content(
        val tracksDurationString: String,
        val tracksCountString: String,
        val tracks: List<PlaylistTrack>
    ): PlaylistEditState
}