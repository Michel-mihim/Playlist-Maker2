package com.example.playlist_maker2.lib.domain.impl

import com.example.playlist_maker2.lib.domain.api.PlaylistsInteractor
import com.example.playlist_maker2.lib.domain.api.PlaylistsRepository
import com.example.playlist_maker2.lib.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsInteractorImpl(
    private val playlistsRepository: PlaylistsRepository
) : PlaylistsInteractor {

    override suspend fun addPlaylist(
        playlist: Playlist,
        onGetResult: (Long) -> Unit
        ) {
        playlistsRepository.addPlaylist(playlist, onGetResult)
    }

    override suspend fun setPlaylistTracksCount(playlistName: String, playlistTracksCount: Int) {
        playlistsRepository.setPlaylistTracksCount(
            playlistName = playlistName,
            playlistTracksCount = playlistTracksCount
        )
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }
}