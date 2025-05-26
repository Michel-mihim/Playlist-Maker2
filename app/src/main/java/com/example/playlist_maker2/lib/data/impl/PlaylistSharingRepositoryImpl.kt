package com.example.playlist_maker2.lib.data.impl

import android.content.Intent
import com.example.playlist_maker2.lib.domain.api.PlaylistSharingRepository

class PlaylistSharingRepositoryImpl(
    private val playlistShareIntent: Intent,
    private val chooser: Intent
) : PlaylistSharingRepository {

    override fun sharePlaylist(text: String, onChooserReady: (Any) -> Unit) {
        playlistShareIntent.setType("text/plain")
        playlistShareIntent.putExtra(Intent.EXTRA_SUBJECT, "My playlist")
        playlistShareIntent.putExtra(Intent.EXTRA_TEXT, text)
        onChooserReady.invoke(chooser)
    }
}