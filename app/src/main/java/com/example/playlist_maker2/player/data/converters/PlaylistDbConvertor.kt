package com.example.playlist_maker2.player.data.converters

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.player.data.db.entities.PlaylistEntity
import com.example.playlist_maker2.player.data.db.entities.TrackEntity
import com.example.playlist_maker2.search.domain.models.Track
import java.time.LocalDateTime
import java.time.ZoneOffset

class PlaylistDbConvertor {

    @RequiresApi(Build.VERSION_CODES.O)
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistPic,
            playlist.playlistName,
            playlist.playlistAbout,
            LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.playlistPic,
            playlist.playlistName,
            playlist.playlistAbout
        )
    }
}