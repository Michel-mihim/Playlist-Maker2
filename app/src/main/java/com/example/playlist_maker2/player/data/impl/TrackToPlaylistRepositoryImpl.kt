package com.example.playlist_maker2.player.data.impl

import com.example.playlist_maker2.lib.domain.models.PlaylistEditState
import com.example.playlist_maker2.lib.domain.models.PlaylistTrack
import com.example.playlist_maker2.player.data.converters.PlaylistTrackDbConvertor
import com.example.playlist_maker2.player.data.db.AppDatabase
import com.example.playlist_maker2.player.data.db.entities.PlaylistTrackEntity
import com.example.playlist_maker2.player.domain.api.TrackToPlaylistRepository
import com.example.playlist_maker2.utils.converters.phraseDurationGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.playlist_maker2.utils.converters.phraseTrackGenerator

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
        val tracksDuration = appDatabase.playlistTracksDao().getTracksDuration(playlistTrackEntity.playlistName) ?: 0
        onGetResult.invoke(result, tracksCount, tracksDuration)
    }

    override suspend fun deleteTrack(
        trackId: String,
        playlistName: String,
        onGetResult: (Int, Int) -> Unit
    ) {
        appDatabase.playlistTracksDao().deletePlaylistTrack(
            trackId = trackId,
            playlistName = playlistName
        )
        val tracksCount = appDatabase.playlistTracksDao().getTracksCount(playlistName)
        val tracksDuration = appDatabase.playlistTracksDao().getTracksDuration(playlistName) ?: 0
        onGetResult.invoke(tracksCount, tracksDuration)
    }

    override suspend fun deletePlaylist(playlistName: String) {
        appDatabase.playlistTracksDao().deletePlaylist(playlistName)
    }

    override suspend fun setPlaylistName(oldPlaylistName: String, newPlaylistName: String) {
        appDatabase.playlistTracksDao().setPlaylistName(
            oldPlaylistName = oldPlaylistName,
            newPlaylistName = newPlaylistName
        )
    }

    override fun getTracks(playlistName: String): Flow<List<PlaylistTrack>> = flow {
        val tracks = appDatabase.playlistTracksDao().getEditTracks(playlistName)
        emit(convertFromPlaylistTrackEntity(tracks))
    }

    override fun getState(playlistName: String): Flow<PlaylistEditState> = flow {
        val tracks = appDatabase.playlistTracksDao().getEditTracks(playlistName)
        val playlistAbout = appDatabase.playlistDao().readPlaylistAbout(playlistName)
        val tracksCountString = phraseTrackGenerator(appDatabase.playlistTracksDao().getTracksCount(playlistName))
        val tracksDurationString = phraseDurationGenerator((appDatabase.playlistTracksDao().getTracksDuration(playlistName) ?: 0))

        emit(PlaylistEditState.Content(playlistAbout, tracksDurationString, tracksCountString, convertFromPlaylistTrackEntity(tracks)))
    }

    private fun convertToPlaylistTrackEntity(playlistTrack: PlaylistTrack): PlaylistTrackEntity {
        return playlistTrackDbConvertor.map(playlistTrack)
    }

    private fun convertFromPlaylistTrackEntity(tracks: List<PlaylistTrackEntity>): List<PlaylistTrack> {
        return tracks.map { track ->  playlistTrackDbConvertor.map(track) }
    }

}