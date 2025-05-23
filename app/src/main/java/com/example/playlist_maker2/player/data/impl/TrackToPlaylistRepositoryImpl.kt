package com.example.playlist_maker2.player.data.impl

import android.util.Log
import com.example.playlist_maker2.lib.domain.models.PlaylistEditState
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
        val tracksDurationString = phraseMinuteGenerator((appDatabase.playlistTracksDao().getTracksDuration(playlistName) ?: 0) / 1000)

        emit(PlaylistEditState.Content(playlistAbout, tracksDurationString, tracksCountString, convertFromPlaylistTrackEntity(tracks)))
    }

    private fun convertToPlaylistTrackEntity(playlistTrack: PlaylistTrack): PlaylistTrackEntity {
        return playlistTrackDbConvertor.map(playlistTrack)
    }

    private fun convertFromPlaylistTrackEntity(tracks: List<PlaylistTrackEntity>): List<PlaylistTrack> {
        return tracks.map { track ->  playlistTrackDbConvertor.map(track) }
    }

    private fun phraseTrackGenerator(tracksCount: Int?): String {
        var word = ""
        var preLastChar: Char? = null
        var lastChar: Char? = null

        lastChar = tracksCount.toString().last()
        if (tracksCount.toString().length >= 2) {
            preLastChar = tracksCount.toString()[tracksCount.toString().length - 2]
        }

        when (preLastChar) {
            '1' -> {
                word = tracksCount.toString() + " треков"
            }

            else -> {
                when (lastChar) {
                    '1' -> word = tracksCount.toString() + " трек"
                    '2' -> word = tracksCount.toString() + " трека"
                    '3' -> word = tracksCount.toString() + " трека"
                    '4' -> word = tracksCount.toString() + " трека"
                    else -> word = tracksCount.toString() + " треков"
                }
            }
        }
        return word
    }

    private fun phraseMinuteGenerator(duration: Int?): String {
        var word = ""
        var preLastChar: Char? = null
        var lastChar: Char? = null

        lastChar = duration.toString().last()
        if (duration.toString().length >= 2) {
            preLastChar = duration.toString()[duration.toString().length - 2]
        }

        when (preLastChar) {
            '1' -> {
                word = duration.toString() + " минут"
            }

            else -> {
                when (lastChar) {
                    '1' -> word = duration.toString() + " минута"
                    '2' -> word = duration.toString() + " минуты"
                    '3' -> word = duration.toString() + " минуты"
                    '4' -> word = duration.toString() + " минуты"
                    else -> word = duration.toString() + " минут"
                }
            }
        }
        return word
    }

}