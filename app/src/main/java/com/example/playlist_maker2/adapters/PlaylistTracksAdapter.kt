package com.example.playlist_maker2.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker2.R
import com.example.playlist_maker2.lib.domain.models.PlaylistTrack

class PlaylistTracksAdapter() : RecyclerView.Adapter<PlaylistTrackViewHolder>() {

    var tracks = ArrayList<PlaylistTrack>()

    var onItemClickListener: ((PlaylistTrack) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return PlaylistTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistTrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        //слушатель нажатия на трек в результатах поиска
        holder.itemView.setOnClickListener {
            //передача трека в активити
            val track: PlaylistTrack = tracks[position]
            onItemClickListener?.invoke(track)
        }
    }

    override fun getItemCount() = tracks.size
}