package com.example.playlist_maker2.player.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker2.R
import com.example.playlist_maker2.adapters.BottomPlaylistsAdapter
import com.example.playlist_maker2.databinding.ActivityPlayerBinding
import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.lib.domain.models.PlaylistTrack
import com.example.playlist_maker2.lib.ui.PlaylistNewFragment
import com.example.playlist_maker2.player.domain.NewPlaylistNameLoadNotifier
import com.example.playlist_maker2.player.domain.models.PlayerStatus
import com.example.playlist_maker2.player.domain.models.PlayerActivityState
import com.example.playlist_maker2.search.domain.models.Track
import com.example.playlist_maker2.utils.constants.Constants
import com.example.playlist_maker2.utils.converters.dimensionsFloatToIntConvert
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.Int
import kotlin.String


class PlayerActivity : AppCompatActivity(), NewPlaylistNameLoadNotifier {

    private lateinit var binding: ActivityPlayerBinding

    lateinit var confirmDialog: MaterialAlertDialogBuilder

    private var isNewPlaylistNameLoaded = false

    private var isClickAllowed = true

    private var track: Track? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var adapter: BottomPlaylistsAdapter

    //VAL BY
    private val playerViewModel by viewModel<PlayerViewModel>()

    //основной листинг==============================================================================
    override fun loadUpdate(isLoaded: Boolean) {
        Log.d("wtf", "updated in Activity")
        isNewPlaylistNameLoaded = isLoaded
        Log.d("wtf", isNewPlaylistNameLoaded.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        confirmDialog = MaterialAlertDialogBuilder(this@PlayerActivity)
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which ->
                // ничего не делаем
            }.setNegativeButton("Завершить") { dialog, which ->
                // выходим из окна без сохранения
                supportFragmentManager.popBackStack()
            }

        playerViewModel.observePlaylistsState().observe(this) {
            showPlaylists(it)
        }

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.addTrackToPlaylistBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        playerViewModel.observePlayerActivityCurrentState().observe(this) {
            renderPlayerState(it)
        }

        playerViewModel.observePlayerPlaylistTrackToastState().observe(this) {
            showPlaylistTrackToast(it)
        }

        binding.buttonPlay2.isEnabled = false //пока плеер не готов на нее нельзя нажимать

        //основной листинг
        val bundle = intent.extras
        if (bundle != null) {
            binding.trackPlayerName.text = bundle.getString(Constants.TRACK_NAME_KEY)
            binding.trackArtistName.text = bundle.getString(Constants.ARTIST_NAME_KEY)
            binding.attr12Time.text = bundle.getString(Constants.TRACK_TIME_KEY)
            binding.attr22Album.text = bundle.getString(Constants.TRACK_ALBUM_KEY)
            binding.attr32Year.text = bundle.getString(Constants.TRACK_YEAR_KEY)
            binding.attr42Genre.text = bundle.getString(Constants.TRACK_GENRE_KEY)
            binding.attr52Country.text = bundle.getString(Constants.TRACK_COUNTRY_KEY)
            val trackJson = bundle.getString(Constants.TRACK_JSON)
            track = Gson().fromJson(trackJson, Track::class.java)

            val cornerDp = resources.getDimension(R.dimen.track_poster_corner)
            val cornerPx = dimensionsFloatToIntConvert(cornerDp, this)
            Glide.with(this)
                .load(bundle.getString(Constants.PIC_URL_KEY))
                .placeholder(R.drawable.placeholder_large)
                .transform(RoundedCorners(cornerPx))
                .into(binding.playerTrackImage)
        }

        playerViewModel.likeButtonSet(track)
        playerViewModel.mediaPlayerPrepare(bundle?.getString((Constants.PREVIEW_PIC_URL_KEY)))

        //слушатели нажатий
        binding.playerBackButton.setOnClickListener{//СВОЯ КНОПКА НАЗАД
            finish()
        }

        binding.buttonPlay2.setOnClickListener {
            if (clickDebouncer()) {
                playerViewModel.playbackControl()
            }
        }

        binding.buttonLike3.setOnClickListener {
            if (clickDebouncer()) {
                playerViewModel.onFavoriteClicked(track)
            }
        }

        binding.buttonPlus1.setOnClickListener {
            if (clickDebouncer()) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED

                playerViewModel.showPlaylists()
            }
        }

        binding.overlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.addPlaylistButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            val bundle = Bundle()
            bundle.putString(Constants.FRAGMENT_ORIGIN_KEY, "activity")
            val playlistFragment = PlaylistNewFragment()
            playlistFragment.arguments = bundle

            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.add_playlist_in_player_container_view, playlistFragment)
                    .addToBackStack("new_playlist")
                    .commit()
            }
        }

        binding.bottomPlaylistsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        overlay(true)
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        overlay(true)
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay(false)
                    }
                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1) / 2
            }

        })
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            Log.d("wtf", "из фрагмента вышли, переопределяемся назад")
            onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    finish()
                }
            })
        } else Log.d("wtf", "из фрагмента НЕ вышли, переопределиться не можем")
        super.onBackPressed()
    }

        /*
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
            super.onBackPressed()
        } else {
            if (isNewPlaylistNameLoaded) {
                confirmDialog.show()
            } else {
                supportFragmentManager.popBackStack()
            }
        }

         */

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }

    private fun renderPlayerState(state: PlayerActivityState) {
        trackPlayButtonActivate(state.playerActivityPlayerReadiness)
        renderTrackPlayButton(state.playerActivityPlayerStatus)
        setTrackProgress(state.playerActivityTrackProgress)
        setLikeButtonLiked(state.playerActivityLikeButtonLiked)
    }

    private fun trackPlayButtonActivate(ready: Boolean) {
        binding.buttonPlay2.isEnabled = ready
    }

    private fun renderTrackPlayButton(status: PlayerStatus) {
        when (status) {
            PlayerStatus.STATE_DEFAULT -> {}
            PlayerStatus.STATE_PREPARED -> setTrackPlayButtonPlay()
            PlayerStatus.STATE_PAUSED -> setTrackPlayButtonPlay()
            PlayerStatus.STATE_PLAYING -> setTrackPlayButtonPause()
        }
    }

    private fun setTrackPlayButtonPlay() {
        binding.buttonPlay2.setImageResource(R.drawable.track_play)
    }

    private fun setTrackPlayButtonPause() {
        binding.buttonPlay2.setImageResource(R.drawable.track_pause)
    }

    private fun setTrackProgress(progress: String) {
        binding.trackPlayerProgress.text = progress
    }

    private fun setLikeButtonLiked(isLiked: Boolean) {
        if (isLiked) {
            binding.buttonLike3.setImageResource(R.drawable.track_liked)
        } else {
            binding.buttonLike3.setImageResource(R.drawable.track_unliked)
        }
    }

    private fun clickDebouncer() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            lifecycleScope.launch {
                delay(Constants.FAST_CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun overlay(enabled: Boolean) {
        if (enabled) {
            binding.overlay.apply {
                visibility = View.VISIBLE
                focusable = View.FOCUSABLE
                isEnabled = true
                isActivated = true
            }
        } else {
            binding.overlay.apply {
                visibility = View.GONE
                focusable = View.NOT_FOCUSABLE
                isEnabled = false
                isActivated = false
            }
        }
    }

    private fun showPlaylists(playlists: List<Playlist>) {
        adapter = BottomPlaylistsAdapter(this@PlayerActivity)
        binding.bottomPlaylistsRecycler.adapter = adapter
        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        adapter.notifyDataSetChanged()

        val trackId = intent.extras?.getString(Constants.TRACK_ID_KEY)
        val trackDuration = intent.extras?.getInt(Constants.TRACK_DURATION_KEY)
        val trackName = intent.extras?.getString(Constants.TRACK_NAME_KEY)
        val artistName = intent.extras?.getString(Constants.ARTIST_NAME_KEY)
        val trackTimeMillis = intent.extras?.getInt(Constants.TRACK_TIME_KEY)
        val artworkUrl100 = intent.extras?.getString(Constants.PIC_URL_KEY)
        val collectionName = intent.extras?.getString(Constants.TRACK_ALBUM_KEY)
        val releaseDate = intent.extras?.getString(Constants.TRACK_YEAR_KEY)
        val primaryGenreName = intent.extras?.getString(Constants.TRACK_GENRE_KEY)
        val country = intent.extras?.getString(Constants.TRACK_COUNTRY_KEY)
        val previewUrl = intent.extras?.getString(Constants.PREVIEW_PIC_URL_KEY)

        adapter.onPlaylistItemClickListener = { playlistName ->
            if (clickDebouncer()) {
                playerViewModel.addPlaylistTrack(PlaylistTrack(
                    trackId = trackId!!,
                    trackDuration = trackDuration!!,
                    playlistName = playlistName,
                    trackName = trackName!!,
                    artistName = artistName!!,
                    trackTimeMillis = trackTimeMillis!!,
                    artworkUrl100 = artworkUrl100!!,
                    collectionName = collectionName!!,
                    releaseDate = releaseDate!!,
                    primaryGenreName = primaryGenreName!!,
                    country = country!!,
                    previewUrl = previewUrl!!
                ))
            }
        }
    }

    private fun showPlaylistTrackToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

}