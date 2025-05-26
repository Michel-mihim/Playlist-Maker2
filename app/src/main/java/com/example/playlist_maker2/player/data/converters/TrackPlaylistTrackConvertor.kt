package com.example.playlist_maker2.player.data.converters

import com.example.playlist_maker2.lib.domain.models.PlaylistTrack
import com.example.playlist_maker2.search.domain.models.Track

class TrackPlaylistTrackConvertor {

    fun map(playlistTrack: PlaylistTrack): Track {
        return Track(
            playlistTrack.trackId,
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