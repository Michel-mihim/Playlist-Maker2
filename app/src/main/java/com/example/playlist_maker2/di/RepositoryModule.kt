package com.example.playlist_maker2.di

import android.content.Intent
import com.example.playlist_maker2.player.data.impl.MediaPlayerRepositoryImpl
import com.example.playlist_maker2.player.domain.api.MediaPlayerRepository
import com.example.playlist_maker2.player.ui.PlayerActivity
import com.example.playlist_maker2.search.data.impl.HistoryTracksRepositoryImpl
import com.example.playlist_maker2.search.data.impl.PlayerIntentGetterImpl
import com.example.playlist_maker2.search.data.impl.SearchTracksRepositoryImpl
import com.example.playlist_maker2.search.domain.api.HistoryTracksRepository
import com.example.playlist_maker2.search.domain.api.PlayerIntentGetter
import com.example.playlist_maker2.search.domain.api.SearchTracksRepository
import com.example.playlist_maker2.settings.data.impl.SettingsRepositoryImpl
import com.example.playlist_maker2.settings.domain.api.SettingsRepository
import com.example.playlist_maker2.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlist_maker2.sharing.data.impl.TextResourseGetterImpl
import com.example.playlist_maker2.sharing.domain.api.ExternalNavigator
import com.example.playlist_maker2.sharing.domain.api.TextResourseGetter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<SearchTracksRepository> {
        SearchTracksRepositoryImpl(get())
    }

    single<HistoryTracksRepository> {
        HistoryTracksRepositoryImpl(get())
    }

    single<PlayerIntentGetter> {
        PlayerIntentGetterImpl(Intent(androidContext(), PlayerActivity::class.java), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get(), androidContext())
    }

    factory<ExternalNavigator> {
        val shareLinkIntent = Intent(Intent.ACTION_SEND)
        ExternalNavigatorImpl(
            shareLinkIntent,
            Intent.createChooser(shareLinkIntent, null),
            Intent(Intent.ACTION_SENDTO),
            Intent(Intent.ACTION_VIEW)
        )
    }

    factory<TextResourseGetter> {
        TextResourseGetterImpl(androidContext())
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

}
