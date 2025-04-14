package com.example.playlist_maker2.search.data.impl

import com.example.playlist_maker2.search.data.NetworkClient
import com.example.playlist_maker2.search.data.dto.TracksSearchRequest
import com.example.playlist_maker2.search.data.dto.TracksSearchResponse
import com.example.playlist_maker2.search.domain.api.SearchTracksRepository
import com.example.playlist_maker2.search.domain.models.SearchTracksResult
import com.example.playlist_maker2.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTracksRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchTracksRepository {
    override fun searchTracks(expression: String): Flow<SearchTracksResult<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        if (response.resultCode == 200) {

            val tracks = (response as TracksSearchResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }

            if (tracks.isNotEmpty()) {//not empty
                emit(SearchTracksResult.Success(tracks, response.resultCode))
            } else {//empty
                emit(SearchTracksResult.Empty(emptyList(), response.resultCode))
            }

        } else
        {//code!=200
            emit(SearchTracksResult.Failure(emptyList(), response.resultCode))
        }

    }
}