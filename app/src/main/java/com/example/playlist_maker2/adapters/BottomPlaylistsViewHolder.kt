package com.example.playlist_maker2.adapters

import android.content.Context
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker2.R
import com.example.playlist_maker2.lib.domain.models.Playlist
import java.io.File

class BottomPlaylistsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val playlistPicView: ImageView
    private val playlistNameView: TextView
    private val playlistTracksCount: TextView

    init {
        playlistPicView = itemView.findViewById(R.id.bottom_track_image)
        playlistNameView = itemView.findViewById(R.id.bottom_track_name)
        playlistTracksCount = itemView.findViewById(R.id.bottom_tracks_count)
    }

    fun bind(playlist: Playlist, context: Context) {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")
        val file = File(filePath, playlist.playlistName+".jpg")

        if (file.exists()) {
            playlistPicView.setImageURI(file.toUri())
        } else {
            playlistPicView.setImageDrawable(context.getDrawable(R.drawable.placeholder_large))
        }

        playlistNameView.text = playlist.playlistName
        playlistTracksCount.text = (playlist.playlistTracksCount?.toString() ?: "0") + wordModifier(playlist.playlistTracksCount)
    }

    private fun wordModifier(tracksCount: Int?): String {
        var word = ""
        var preLastChar: Char? = null
        var lastChar: Char? = null

        lastChar = tracksCount.toString().last()
        if (tracksCount.toString().length >= 2) {
            preLastChar = tracksCount.toString()[tracksCount.toString().length - 2]
        }

        when (preLastChar) {
            '1' -> {
                word = " треков"
            }

            else -> {
                when (lastChar) {
                    '1' -> word = " трек"
                    '2' -> word = " трека"
                    '3' -> word = " трека"
                    '4' -> word = " трека"
                    else -> word = " треков"
                }
            }
        }
        return word
    }

}