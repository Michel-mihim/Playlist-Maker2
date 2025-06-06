package com.example.playlist_maker2.lib.domain.api

import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun addPlaylist(
        playlist: Playlist,
        onGetResult: (Long) -> Unit
        )

    suspend fun deletePlaylist(playlistName: String)

    suspend fun setPlaylistTracksCalculation(
        playlistName: String,
        playlistTracksCount: Int,
        playlistTracksDuration: Int
    )

    suspend fun setPlaylistInformation(
        oldPlaylistName: String,
        newPlaylistName: String,
        newPlaylistAbout: String
    )

    fun checkPlaylistDuplicate(playlistName: String): Flow<Boolean>

    fun getPlaylists(): Flow<List<Playlist>>

    fun readPlaylist(playlistName: String): Flow<Playlist>
}