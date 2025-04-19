package com.example.playlist_maker2.player.domain.models

import com.example.playlist_maker2.search.domain.models.Track

sealed interface DBActivityState {

    object Empty: DBActivityState

    data class Content(val tracks: List<Track>): DBActivityState
}