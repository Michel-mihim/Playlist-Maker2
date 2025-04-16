package com.example.playlist_maker2.player.data.impl

import com.example.playlist_maker2.player.data.converters.TrackDbConvertor
import com.example.playlist_maker2.player.data.db.AppDatabase
import com.example.playlist_maker2.player.data.db.entities.TrackEntity
import com.example.playlist_maker2.player.domain.api.FavoriteTracksRepository
import com.example.playlist_maker2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor
) : FavoriteTracksRepository {
    override suspend fun addTrack(track: Track) {
        val trackEntity = convertToTrackEntity(track)
        appDatabase.trackDao().insertTrack(trackEntity)
    }

    override suspend fun deleteTrack(track: Track) {
        val trackEntity = convertToTrackEntity(track)
        appDatabase.trackDao().deleteTrack(trackEntity)
    }

    override fun getTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override fun getTracksById(trackId: String): Flow<List<String>> = flow {
        emit(appDatabase.trackDao().getTracksById(trackId))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

    private fun convertToTrackEntity(track: Track): TrackEntity {
        return trackDbConvertor.map(track)
    }
}