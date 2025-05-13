package com.example.playlist_maker2.player.data.converters

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.playlist_maker2.player.data.db.entities.TrackEntity
import com.example.playlist_maker2.search.domain.models.Track
import java.time.LocalDateTime
import java.time.ZoneOffset

class TrackDbConvertor {
    
    @RequiresApi(Build.VERSION_CODES.O)
    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}