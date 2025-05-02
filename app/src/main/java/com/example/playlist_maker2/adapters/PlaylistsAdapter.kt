package com.example.playlist_maker2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker2.R
import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.search.domain.models.Track

class PlaylistsAdapter() : RecyclerView.Adapter<PlaylistViewHolder>() {

    var playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_view, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount() = playlists.size
}