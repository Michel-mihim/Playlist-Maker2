package com.example.playlist_maker2.search.data

import com.example.playlist_maker2.search.data.dto.Response


interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}