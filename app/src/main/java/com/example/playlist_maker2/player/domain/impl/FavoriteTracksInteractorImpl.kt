package com.example.playlist_maker2.player.domain.impl

import com.example.playlist_maker2.player.domain.api.FavoriteTracksInteractor
import com.example.playlist_maker2.player.domain.api.FavoriteTracksRepository
import com.example.playlist_maker2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val favoriteTracksRepository: FavoriteTracksRepository
) : FavoriteTracksInteractor {

    override suspend fun addTrack(track: Track) {
        favoriteTracksRepository.addTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        favoriteTracksRepository.deleteTrack(track)
    }

    override fun getTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.getTracks()
    }

    override fun getTracksById(trackId: String): Flow<List<String>> {
        return favoriteTracksRepository.getTracksById(trackId)
    }
}