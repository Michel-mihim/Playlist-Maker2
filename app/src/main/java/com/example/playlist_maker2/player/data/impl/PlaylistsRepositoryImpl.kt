package com.example.playlist_maker2.player.data.impl

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.playlist_maker2.lib.domain.api.PlaylistsRepository
import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.player.data.converters.PlaylistDbConvertor
import com.example.playlist_maker2.player.data.converters.TrackDbConvertor
import com.example.playlist_maker2.player.data.db.AppDatabase
import com.example.playlist_maker2.player.data.db.entities.PlaylistEntity

class PlaylistsRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
) : PlaylistsRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun addPlaylist(playlist: Playlist) {
        val playlistEntity = convertToPlaylistEntity(playlist)
        appDatabase.playlistDao().insertPlaylist(playlistEntity)
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return playlistDbConvertor.map(playlist)
    }

}