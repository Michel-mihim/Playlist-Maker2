package com.example.playlist_maker2.player.data.converters

import com.example.playlist_maker2.lib.domain.models.PlaylistTrack
import com.example.playlist_maker2.player.data.db.entities.PlaylistTracksEntity

class PlaylistTrackDbConvertor {

    fun map(playlistTrack: PlaylistTrack): PlaylistTracksEntity {
        return PlaylistTracksEntity(
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

    fun map(playlistTrack: PlaylistTracksEntity): PlaylistTrack {
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