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

    override suspend fun deletePlaylist(playlistName: String) {
        playlistsRepository.deletePlaylist(playlistName)
    }

    override suspend fun setPlaylistTracksCalculation(playlistName: String, playlistTracksCount: Int, playlistTracksDuration: Int) {
        playlistsRepository.setPlaylistTracksCalculation(
            playlistName = playlistName,
            playlistTracksCount = playlistTracksCount,
            playlistTracksDuration = playlistTracksDuration
        )
    }

    override suspend fun setPlaylistInformation(
        oldPlaylistName: String,
        newPlaylistName: String,
        newPlaylistAbout: String
    ) {
        playlistsRepository.setPlaylistInformation(
            oldPlaylistName = oldPlaylistName,
            newPlaylistName = newPlaylistName,
            newPlaylistAbout = newPlaylistAbout
        )
    }

    override fun checkPlaylistDuplicate(playlistName: String): Flow<Boolean> {
        return playlistsRepository.checkPlaylistDuplicate(playlistName)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getPlaylists()
    }

    override fun readPlaylist(playlistName: String): Flow<Playlist> {
        return playlistsRepository.readPlaylist(playlistName)
    }
}