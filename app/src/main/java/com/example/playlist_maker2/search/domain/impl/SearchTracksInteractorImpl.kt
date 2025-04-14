package com.example.playlist_maker2.search.domain.impl

import com.example.playlist_maker2.search.domain.api.SearchTracksInteractor
import com.example.playlist_maker2.search.domain.api.SearchTracksRepository
import com.example.playlist_maker2.search.domain.models.SearchTracksResult
import com.example.playlist_maker2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class SearchTracksInteractorImpl(
    private val repository: SearchTracksRepository
): SearchTracksInteractor {

    override fun searchTracks(expression: String): Flow<SearchTracksResult<List<Track>>> {
        return repository.searchTracks(expression)
    }
}