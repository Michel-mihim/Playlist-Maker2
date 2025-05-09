package com.example.playlist_maker2.player.data.impl

import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.lib.domain.models.PlaylistTrack
import com.example.playlist_maker2.player.data.converters.PlaylistTrackDbConvertor
import com.example.playlist_maker2.player.data.db.AppDatabase
import com.example.playlist_maker2.player.data.db.entities.PlaylistEntity
import com.example.playlist_maker2.player.data.db.entities.PlaylistTracksEntity
import com.example.playlist_maker2.player.domain.api.TrackToPlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackToPlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistTrackDbConvertor: PlaylistTrackDbConvertor
) : TrackToPlaylistRepository {

    override suspend fun addTrack(
        playlistTrack: PlaylistTrack,
        onGetResult: (Long) -> Unit
    ) {
        val playlistTrackEntity = convertToPlaylistTrackEntity(playlistTrack)
        val result = appDatabase.playlistTracksDao().insertPlaylistTrack(playlistTrackEntity)
        onGetResult.invoke(result)
    }

    override fun getTracksCount(playlistName: String): Flow<Long> = flow {
        val playlistTracksCount = appDatabase.playlistTracksDao().getTracksCount(playlistName)
        emit(playlistTracksCount)
    }

    private fun convertToPlaylistTrackEntity(playlistTrack: PlaylistTrack): PlaylistTracksEntity {
        return playlistTrackDbConvertor.map(playlistTrack)
    }

}