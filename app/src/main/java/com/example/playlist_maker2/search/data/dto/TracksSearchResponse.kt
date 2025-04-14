package com.example.playlist_maker2.search.data.dto

import com.example.playlist_maker2.search.data.dto.Response
import com.example.playlist_maker2.search.data.dto.TrackDto

data class TracksSearchResponse(
    val resultCount: Int,
    val results: ArrayList<TrackDto>
) : Response()