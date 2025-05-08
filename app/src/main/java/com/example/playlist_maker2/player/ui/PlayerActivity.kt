package com.example.playlist_maker2.player.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker2.R
import com.example.playlist_maker2.adapters.BottomPlaylistsAdapter
import com.example.playlist_maker2.databinding.ActivityPlayerBinding
import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.player.domain.models.PlayerStatus
import com.example.playlist_maker2.player.domain.models.PlayerActivityState
import com.example.playlist_maker2.search.domain.models.Track
import com.example.playlist_maker2.utils.constants.Constants
import com.example.playlist_maker2.utils.converters.dimensionsFloatToIntConvert
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private var isClickAllowed = true

    private var track: Track? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var adapter: BottomPlaylistsAdapter

    //VAL BY
    private val playerViewModel by viewModel<PlayerViewModel>()

    //основной листинг==============================================================================
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        binding.playerBackButton.setOnClickListener{
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
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                blackout(true) //тут в самый раз

                playerViewModel.showPlaylists()
            }
        }

        binding.blackout.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.bottomPlaylistsRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {} //тут слишком поздно для blackout
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        blackout(false)
                    }
                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

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
    fun blackout(enabled: Boolean) {
        if (enabled) {
            binding.blackout.apply {
                alpha = 0.5f
                visibility = View.VISIBLE
                focusable = View.FOCUSABLE
                isEnabled = true
                isActivated = true
            }
        } else {
            binding.blackout.apply {
                alpha = 0f
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
    }

}