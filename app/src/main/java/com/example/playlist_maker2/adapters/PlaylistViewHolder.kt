package com.example.playlist_maker2.adapters

import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker2.R
import com.example.playlist_maker2.lib.domain.models.Playlist
import java.io.File

class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val playlistPicView: ImageView
    private val playlistNameView: TextView
    private val playlistAboutView: TextView

    init {
        playlistPicView = itemView.findViewById(R.id.new_playlist_picture)
        playlistNameView = itemView.findViewById(R.id.new_playlist_name)
        playlistAboutView = itemView.findViewById(R.id.new_playlist_about)
    }

    fun bind(playlist: Playlist, context: Context) {
        try {
            val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")
            val file = File(filePath, playlist.playlistName+".jpg")
            playlistPicView.setImageURI(file.toUri())
        } catch (e: Exception) {Log.d("wtf", "error")}

        playlistNameView.text = playlist.playlistName
        playlistAboutView.text = playlist.playlistAbout
    }

}