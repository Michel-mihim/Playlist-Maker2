package com.example.playlist_maker2.search.data.impl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.playlist_maker2.search.domain.api.PlayerIntentGetter
import com.example.playlist_maker2.search.domain.models.Track
import com.example.playlist_maker2.utils.constants.Constants
import com.example.playlist_maker2.utils.converters.getCoverArtwork
import com.example.playlist_maker2.utils.converters.isoDateToYearConvert
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerIntentGetterImpl(
    private val playerIntent: Intent,
    private val bundle: Bundle
) : PlayerIntentGetter {
    override fun getPlayerIntent(
        track: Track,
        onPlayerIntentReady: ((Any) -> Unit)
    ) {
        bundle.putString(Constants.TRACK_ID_KEY, track.trackId)
        bundle.putString(Constants.TRACK_NAME_KEY, track.trackName)
        bundle.putString(Constants.ARTIST_NAME_KEY, track.artistName)
        bundle.putInt(Constants.TRACK_DURATION_KEY, track.trackTimeMillis)
        bundle.putInt(Constants.TRACK_TIME_KEY, track.trackTimeMillis)
        bundle.putString(Constants.PIC_URL_KEY, getCoverArtwork(track.artworkUrl100))
        bundle.putString(Constants.TRACK_ALBUM_KEY, track.collectionName)
        bundle.putString(Constants.TRACK_YEAR_KEY, isoDateToYearConvert(track.releaseDate))
        bundle.putString(Constants.TRACK_GENRE_KEY, track.primaryGenreName)
        bundle.putString(Constants.TRACK_COUNTRY_KEY, track.country)
        bundle.putString(Constants.PREVIEW_PIC_URL_KEY, track.previewUrl)
        val trackJson = Gson().toJson(track)
        bundle.putString(Constants.TRACK_JSON, trackJson)
        playerIntent.putExtras(bundle)
        onPlayerIntentReady.invoke(playerIntent)
    }
}