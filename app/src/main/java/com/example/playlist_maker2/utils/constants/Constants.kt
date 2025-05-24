package com.example.playlist_maker2.utils.constants

import android.app.Activity

object Constants {
    const val SEARCH_STRING = "SEARCH_STRING"
    const val SEARCH_DEF = ""
    const val TRACKS_NOT_FOUND = "Ничего не нашлось"
    const val TRACKS_NOT_FOUND_2 = "Ничего не найдено"
    const val NETWORK_PROBLEM = "Проблемы со связью\n" +
            "\n" +
            "Загрузка не удалась. Проверьте подключение к интернету"
    const val SOMETHING_WRONG = "Что-то пошло не так.."
    const val SEARCH_SUCCESS = "Поиск успешно произведен!"
    const val HISTORY_CLEARED = "История поиска была удалена"
    const val READ_MEDIA_IMAGES_DENIED = "Доступ к изображениям на устройстве не получен"
    const val EMAIL_CLIENT_NOT_FOUND = "Не обнаружен почтовый клиент!"

    const val SEARCH_DEBOUNCE_DELAY = 2000L
    const val CLICK_DEBOUNCE_DELAY = 1000L
    const val FAST_CLICK_DEBOUNCE_DELAY = 500L

    //настройки
    const val PREFERENCES = "shared_preferences"

    const val THEME_KEY = "night_theme"
    const val SEARCH_HISTORY_KEY = "history"

    const val HISTORY_CAPACITY = 10

    //медиатека


    //медиаплеер
    const val SHOW_PROGRESS_DELAY = 300L
    const val TRACK_IS_OVER_PROGRESS = "00:00"
    const val FRAGMENT_ORIGIN_KEY = "origin"

    //ключи track
    const val TRACK_ID_KEY = "b_track_id"
    const val TRACK_NAME_KEY = "b_track_name"
    const val ARTIST_NAME_KEY = "b_artist_name"
    const val TRACK_DURATION_KEY = "b_track_duration"
    const val TRACK_TIME_KEY = "b_track_time"
    const val TRACK_ALBUM_KEY = "b_track_album"
    const val TRACK_YEAR_KEY = "b_track_year"
    const val TRACK_GENRE_KEY = "b_track_genre"
    const val TRACK_COUNTRY_KEY = "b_track_country"
    const val PIC_URL_KEY = "b_artworkUrl100"
    const val PREVIEW_PIC_URL_KEY = "b_previewUrl"
    const val TRACK_JSON = "b_track_json"

    //ключи playlist
    const val PLAYLIST_NAME_KEY = "playlist_name"
    const val PLAYLIST_ABOUT_KEY = "playlist_about"
    const val PLAYLIST_CAPACITY_KEY = "playlist_capacity"

    //NOTIFICATIONS
    const val PLAYLIST_DUPLICATE_ERROR = "Плейлист уже существует"

}