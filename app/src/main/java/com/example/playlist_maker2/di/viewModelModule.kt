package com.example.playlist_maker2.di

import com.example.playlist_maker2.lib.ui.FavoriteViewModel
import com.example.playlist_maker2.lib.ui.PlaylistEditViewModel
import com.example.playlist_maker2.lib.ui.PlaylistNewViewModel
import com.example.playlist_maker2.lib.ui.PlaylistViewModel
import com.example.playlist_maker2.search.ui.SearchViewModel
import com.example.playlist_maker2.settings.ui.SettingsViewModel
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get(), androidApplication())
    }

    viewModel {
        PlayerViewModel(get(), get(), get(), get())
    }

    viewModel {
        FavoriteViewModel(get(), get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel {
        PlaylistNewViewModel(get())
    }

    viewModel {
        PlaylistEditViewModel(get(), get(), get())
    }

}