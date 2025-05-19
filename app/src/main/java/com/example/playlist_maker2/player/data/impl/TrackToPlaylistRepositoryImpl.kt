package com.example.playlist_maker2.player.data.impl

import android.util.Log
import com.example.playlist_maker2.lib.domain.models.PlaylistTrack
import com.example.playlist_maker2.player.data.converters.PlaylistTrackDbConvertor
import com.example.playlist_maker2.player.data.db.AppDatabase
import com.example.playlist_maker2.player.data.db.entities.PlaylistTrackEntity
import com.example.playlist_maker2.player.domain.api.TrackToPlaylistRepository
import com.example.playlist_maker2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackToPlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistTrackDbConvertor: PlaylistTrackDbConvertor
) : TrackToPlaylistRepository {

    override suspend fun addTrack(
        playlistTrack: PlaylistTrack,
        onGetResult: (Long, Int, Int) -> Unit
    ) {
        val playlistTrackEntity = convertToPlaylistTrackEntity(playlistTrack)
        val result = appDatabase.playlistTracksDao().insertPlaylistTrack(playlistTrackEntity)
        val tracksCount = appDatabase.playlistTracksDao().getTracksCount(playlistTrackEntity.playlistName)
        val tracksDuration = appDatabase.playlistTracksDao().getTracksDuration(playlistTrackEntity.playlistName)
        onGetResult.invoke(result, tracksCount, tracksDuration)
    }

    override fun getTracks(playlistName: String): Flow<List<PlaylistTrack>> = flow {
        val tracks = appDatabase.playlistTracksDao().getEditTracks(playlistName)
        emit(convertFromPlaylistTrackEntity(tracks))
    }

    private fun convertToPlaylistTrackEntity(playlistTrack: PlaylistTrack): PlaylistTrackEntity {
        return playlistTrackDbConvertor.map(playlistTrack)
    }

    private fun convertFromPlaylistTrackEntity(tracks: List<PlaylistTrackEntity>): List<PlaylistTrack> {
        return tracks.map { track ->  playlistTrackDbConvertor.map(track) }
    }

}