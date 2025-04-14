package com.example.playlist_maker2.player.domain.models

data class PlayerActivityState(
    val playerActivityPlayerReadiness: Boolean,
    val playerActivityPlayerStatus: PlayerStatus,
    val playerActivityTrackProgress: String
)