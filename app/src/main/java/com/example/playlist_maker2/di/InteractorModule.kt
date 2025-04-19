package com.example.playlist_maker2.di


import com.example.playlist_maker2.player.domain.api.FavoriteTracksInteractor
import com.example.playlist_maker2.player.domain.api.MediaPlayerInteractor
import com.example.playlist_maker2.player.domain.impl.FavoriteTracksInteractorImpl
import com.example.playlist_maker2.player.domain.impl.MediaPlayerInteractorImpl
import com.example.playlist_maker2.search.domain.api.GetPlayerIntentUseCase
import com.example.playlist_maker2.search.domain.api.HistoryTracksInteractor
import com.example.playlist_maker2.search.domain.api.SearchTracksInteractor
import com.example.playlist_maker2.search.domain.impl.HistoryTracksInteractorImpl
import com.example.playlist_maker2.search.domain.impl.SearchTracksInteractorImpl
import com.example.playlist_maker2.settings.domain.api.SettingsInteractor
import com.example.playlist_maker2.settings.domain.impl.SettingsInteractorImpl
import com.example.playlist_maker2.sharing.domain.api.SharingInteractor
import com.example.playlist_maker2.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    single<SearchTracksInteractor> {
        SearchTracksInteractorImpl(get())
    }

    single<HistoryTracksInteractor> {
        HistoryTracksInteractorImpl(get())
    }

    factory {
        GetPlayerIntentUseCase(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get())
    }

    factory<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }

}