package com.example.playlist_maker2.search.data.network

import com.example.playlist_maker2.search.data.NetworkClient
import com.example.playlist_maker2.search.data.dto.Response
import com.example.playlist_maker2.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val iTunesApiService: ITunesApiService
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto is TracksSearchRequest) {
            return withContext(Dispatchers.IO) {
                try {
                    val retrofitResp = iTunesApiService.searchTracks(dto.expression)
                    retrofitResp.apply { resultCode = 200 }
                } catch (e: Throwable) {
                    Response().apply { resultCode = 500 }
                }
            }
        } else
            return Response().apply { resultCode = 400 } //если запрос от нас не корректный; событие маловероятно
    }
}