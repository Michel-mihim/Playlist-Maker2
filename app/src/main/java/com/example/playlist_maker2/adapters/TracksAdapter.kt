package com.example.playlist_maker2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlist_maker2.R
import com.example.playlist_maker2.search.domain.models.Track
import com.example.playlist_maker2.adapters.TrackViewHolder

class TracksAdapter() : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()

    var onItemClickListener: ((Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        //слушатель нажатия на трек в результатах поиска
        holder.itemView.setOnClickListener {
            //передача трека в активити
            val track: Track = tracks[position]
            onItemClickListener?.invoke(track)
        }
    }

    override fun getItemCount() = tracks.size

}