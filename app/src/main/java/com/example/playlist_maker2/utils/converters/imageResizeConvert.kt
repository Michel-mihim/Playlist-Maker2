package com.example.playlist_maker2.utils.converters

fun getCoverArtwork(artworkUrl100: String): String {
    return artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}