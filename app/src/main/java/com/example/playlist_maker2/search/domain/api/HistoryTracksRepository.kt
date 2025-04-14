package com.example.playlist_maker2.search.domain.api

import com.example.playlist_maker2.search.domain.OnHistoryUpdatedListener
import com.example.playlist_maker2.search.domain.models.Track

interface HistoryTracksRepository {
    fun getTracks(): Array<Track>
    fun addTrack(track: Track)
    fun clearTracks()
    fun setOnHistoryUpdatedListener(onHistoryUpdatedListener: OnHistoryUpdatedListener)
}