package com.example.playlist_maker2.search.domain.impl

import com.example.playlist_maker2.search.domain.OnHistoryUpdatedListener
import com.example.playlist_maker2.search.domain.api.HistoryTracksInteractor
import com.example.playlist_maker2.search.domain.api.HistoryTracksRepository
import com.example.playlist_maker2.search.domain.models.Track

class HistoryTracksInteractorImpl(
    private val repository: HistoryTracksRepository
) :
    HistoryTracksInteractor {

    override fun getTracks(): Array<Track> {
        return repository.getTracks()
    }

    override fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override fun clearTracks() {
        repository.clearTracks()
    }

    override fun setOnHistoryUpdatedListener(onHistoryUpdatedListener: OnHistoryUpdatedListener) {
        repository.setOnHistoryUpdatedListener(onHistoryUpdatedListener)
    }

}