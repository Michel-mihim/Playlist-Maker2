package com.example.playlist_maker2.player.domain.api

import com.example.playlist_maker2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {

    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(track: Track)
    fun getTracks(): Flow<List<Track>>
    fun getTracksById(trackId: String): Flow<List<String>>
}