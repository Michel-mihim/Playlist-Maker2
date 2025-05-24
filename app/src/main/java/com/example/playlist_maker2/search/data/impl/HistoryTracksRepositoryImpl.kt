package com.example.playlist_maker2.search.data.impl

import android.content.SharedPreferences
import android.util.Log
import com.example.playlist_maker2.search.domain.OnHistoryUpdatedListener
import com.example.playlist_maker2.search.domain.api.HistoryTracksRepository
import com.example.playlist_maker2.search.domain.models.Track
import com.example.playlist_maker2.utils.constants.Constants
import com.google.gson.Gson

class HistoryTracksRepositoryImpl(
    private val sharedPrefs: SharedPreferences
) : HistoryTracksRepository {

    var listener: OnHistoryUpdatedListener? = null

    override fun getTracks(): Array<Track> {
        val json = sharedPrefs.getString(Constants.SEARCH_HISTORY_KEY, null) ?: return emptyArray()
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    override fun addTrack(track: Track) {
        val newTracks = ArrayList<Track>()
        val lastTracks = getTracks()
        newTracks.clear()
        newTracks.addAll(newHistoryGenerator(track, lastTracks))
        val json = Gson().toJson(newTracks)
        sharedPrefs.edit()
            .putString(Constants.SEARCH_HISTORY_KEY, json)
            .apply()

        this.listener?.onHistoryUpdated()
    }

    override fun clearTracks() {
        sharedPrefs.edit()
            .remove(Constants.SEARCH_HISTORY_KEY)
            .apply()

        this.listener?.onHistoryUpdated()
    }

    override fun setOnHistoryUpdatedListener(onHistoryUpdatedListener: OnHistoryUpdatedListener) {
        this.listener = onHistoryUpdatedListener
    }
    //==============================================================================================

    private fun newHistoryGenerator(trackToAdd: Track, tracks: Array<Track>): ArrayList<Track> {
        val newTracks = ArrayList<Track>()
        trackInListChecker(
            trackToAdd, tracks,
            onPresent = {newTracks.addAll(listWithMadeTrackFirstIfPresent(trackToAdd, tracks))},
            onAbsent = {newTracks.addAll(listWithAddedTrackFirstIfAbsent(trackToAdd, tracks))}
        )
        return newTracks.take(Constants.HISTORY_CAPACITY).toCollection(ArrayList())
    }

    private fun trackInListChecker(trackAdded: Track, tracks: Array<Track>,
                                   onPresent: (Boolean) -> Unit,
                                   onAbsent: (Boolean) -> Unit
    ) {
        if (isTrackInList(getTrackId(trackAdded), tracks)) {
            onPresent(true)
        } else {
            onAbsent(false)
        }
    }

    private fun listWithAddedTrackFirstIfAbsent(trackAdded: Track, tracks: Array<Track>): ArrayList<Track> {
        val tracksWhereNewFirst = ArrayList<Track>()
        tracksWhereNewFirst.add(trackAdded)
        tracksWhereNewFirst.addAll(tracks)
        return tracksWhereNewFirst
    }

    private fun listWithMadeTrackFirstIfPresent(trackAdded: Track, tracks: Array<Track>): ArrayList<Track> {
        val tracksWhereDuplicateTrackFirst = ArrayList<Track>()
        val tracksList: MutableList<Track> = ArrayList()
        tracksList.addAll(tracks)
        tracksList.removeAt(getDuplicateTrackPos(trackAdded, tracks))
        tracksWhereDuplicateTrackFirst.add(trackAdded)
        tracksWhereDuplicateTrackFirst.addAll(tracksList)
        return tracksWhereDuplicateTrackFirst
    }

    private fun getTrackId(track: Track): String {
        return track.trackId
    }

    private fun isTrackInList(trackId: String, tracks: Array<Track>): Boolean {
        for (track in tracks) {
            if (getTrackId(track) == trackId) {
                return true
            }
        }
        return false
    }

    private fun getDuplicateTrackPos(trackAdded: Track, tracks: Array<Track>): Int {
        var pos = 0
        for (i in 0..<tracks.size) {
            if (trackAdded.trackId == tracks[i].trackId) pos = i
        }
        return pos
    }

}