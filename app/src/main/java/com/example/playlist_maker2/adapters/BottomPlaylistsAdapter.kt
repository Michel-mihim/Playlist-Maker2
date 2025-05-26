package com.example.playlist_maker2.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker2.R
import com.example.playlist_maker2.lib.domain.models.Playlist

class BottomPlaylistsAdapter(val context: Context) : RecyclerView.Adapter<BottomPlaylistsViewHolder>() {

    var playlists = ArrayList<Playlist>()

    var onPlaylistItemClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomPlaylistsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_bottom_view, parent, false)
        return BottomPlaylistsViewHolder(view)
    }

    override fun onBindViewHolder(holder: BottomPlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position], context)

        holder.itemView.setOnClickListener {
            val playlistName = playlists[position].playlistName

            onPlaylistItemClickListener?.invoke(playlistName)
        }

    }

    override fun getItemCount(): Int = playlists.size
}