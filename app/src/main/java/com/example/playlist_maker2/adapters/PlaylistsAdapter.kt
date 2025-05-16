package com.example.playlist_maker2.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker2.R
import com.example.playlist_maker2.lib.domain.models.Playlist

class PlaylistsAdapter(val context: Context) : RecyclerView.Adapter<PlaylistViewHolder>() {

    var playlists = ArrayList<Playlist>()

    var onItemClickListener: ((Playlist) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_view, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position], context)

        holder.itemView.setOnClickListener {
            val playlist = playlists[position]
            Log.d("wtf", "in adapter "+playlist.toString())
        }

    }

    override fun getItemCount() = playlists.size
}