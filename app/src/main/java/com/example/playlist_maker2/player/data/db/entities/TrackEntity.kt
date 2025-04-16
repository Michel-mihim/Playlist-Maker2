package com.example.playlist_maker2.player.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.INTEGER
import androidx.room.ColumnInfo.Companion.TEXT
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey @ColumnInfo(name = "trackId", typeAffinity = TEXT)
    val trackId:String,
    @ColumnInfo(name = "trackName", typeAffinity = TEXT)
    val trackName: String,
    @ColumnInfo(name = "artistName", typeAffinity = TEXT)
    val artistName: String,
    @ColumnInfo(name = "trackTimeMillis", typeAffinity = INTEGER)
    val trackTimeMillis: Int,
    @ColumnInfo(name = "artworkUrl100", typeAffinity = TEXT)
    val artworkUrl100: String,
    @ColumnInfo(name = "collectionName", typeAffinity = TEXT)
    val collectionName: String,
    @ColumnInfo(name = "releaseDate", typeAffinity = TEXT)
    val releaseDate: String,
    @ColumnInfo(name = "primaryGenreName", typeAffinity = TEXT)
    val primaryGenreName: String,
    @ColumnInfo(name = "country", typeAffinity = TEXT)
    val country: String,
    @ColumnInfo(name = "previewUrl", typeAffinity = TEXT)
    val previewUrl: String
)
