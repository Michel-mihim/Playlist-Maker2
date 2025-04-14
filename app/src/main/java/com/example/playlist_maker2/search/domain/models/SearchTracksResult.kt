package com.example.playlist_maker2.search.domain.models

sealed class SearchTracksResult<T>(val tracks: T, val code: Int) {
    class Success<T>(tracks: T, code: Int): SearchTracksResult<T>(tracks, code){}
    class Empty<T>(tracks: T, code: Int): SearchTracksResult<T>(tracks, code){}
    class Failure<T>(tracks: T, code: Int): SearchTracksResult<T>(tracks, code){}
}