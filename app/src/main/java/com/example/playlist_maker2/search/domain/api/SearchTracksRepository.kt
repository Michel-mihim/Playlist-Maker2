package com.example.playlist_maker2.search.domain.api

import com.example.playlist_maker2.search.domain.models.SearchTracksResult
import com.example.playlist_maker2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchTracksRepository {
    fun searchTracks(
        expression: String
    ): Flow<SearchTracksResult<List<Track>>>
}