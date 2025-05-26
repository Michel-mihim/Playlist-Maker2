package com.example.playlist_maker2.player.data.converters

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.playlist_maker2.lib.domain.models.PlaylistTrack
import com.example.playlist_maker2.player.data.db.entities.PlaylistTrackEntity
import java.time.LocalDateTime
import java.time.ZoneOffset

class PlaylistTrackDbConvertor {

    @RequiresApi(Build.VERSION_CODES.O)
    fun map(playlistTrack: PlaylistTrack): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            playlistTrack.trackId,
            playlistTrack.trackDuration,
            playlistTrack.playlistName,
            playlistTrack.trackName,
            playlistTrack.artistName,
            playlistTrack.trackTimeMillis,
            playlistTrack.artworkUrl100,
            playlistTrack.collectionName,
            playlistTrack.releaseDate,
            playlistTrack.primaryGenreName,
            playlistTrack.country,
            playlistTrack.previewUrl,
            LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        )
    }

    fun map(playlistTrack: PlaylistTrackEntity): PlaylistTrack {
        return PlaylistTrack(
            playlistTrack.trackId,
            playlistTrack.trackDuration,
            playlistTrack.playlistName,
            playlistTrack.trackName,
            playlistTrack.artistName,
            playlistTrack.trackTimeMillis,
            playlistTrack.artworkUrl100,
            playlistTrack.collectionName,
            playlistTrack.releaseDate,
            playlistTrack.primaryGenreName,
            playlistTrack.country,
            playlistTrack.previewUrl
        )
    }
}