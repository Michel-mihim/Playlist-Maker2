package com.example.playlist_maker2.di

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.room.Room
import com.example.playlist_maker2.player.data.db.AppDatabase
import com.example.playlist_maker2.search.data.NetworkClient
import com.example.playlist_maker2.search.data.network.ITunesApiService
import com.example.playlist_maker2.search.data.network.RetrofitNetworkClient
import com.example.playlist_maker2.utils.constants.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ITunesApiService> {
        val iTunesBaseUrl = "https://itunes.apple.com"
        Retrofit.Builder()
            .baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single {
        androidContext().
        getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE)
    }

    factory {
        Bundle()
    }

    factory {
        MediaPlayer()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }

}