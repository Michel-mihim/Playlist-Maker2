package com.example.playlist_maker2.player.data.impl

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.playlist_maker2.lib.domain.api.PlaylistsRepository
import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.player.data.converters.PlaylistDbConvertor
import com.example.playlist_maker2.player.data.converters.TrackDbConvertor
import com.example.playlist_maker2.player.data.db.AppDatabase
import com.example.playlist_maker2.player.data.db.entities.PlaylistEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
) : PlaylistsRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addPlaylist(
        playlist: Playlist,
        onGetResult: (Long) -> Unit
        ) {
        val playlistEntity = convertToPlaylistEntity(playlist)
        val result = appDatabase.playlistDao().insertPlaylist(playlistEntity)
        onGetResult.invoke(result)
    }

    override suspend fun deletePlaylist(playlistName: String) {
        appDatabase.playlistDao().deletePlaylist(playlistName)
    }

    override suspend fun setPlaylistTracksCalculation(
        playlistName: String,
        playlistTracksCount: Int,
        playlistTracksDuration: Int
    ) {
        appDatabase.playlistDao().setPlaylistTracksCalculation(
            playlistName = playlistName,
            playlistTracksCount = playlistTracksCount,
            playlistTracksDuration = playlistTracksDuration
            )
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return playlistDbConvertor.map(playlist)
    }

}