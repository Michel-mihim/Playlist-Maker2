package com.example.playlist_maker2.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlist_maker2.R
import com.example.playlist_maker2.databinding.ActivityPlayerBinding
import com.example.playlist_maker2.player.domain.models.PlayerStatus
import com.example.playlist_maker2.player.domain.models.PlayerActivityState
import com.example.playlist_maker2.utils.constants.Constants
import com.example.playlist_maker2.utils.converters.dimensionsFloatToIntConvert
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private var isClickAllowed = true

    //VAL BY
    private val playerViewModel by viewModel<PlayerViewModel>()

    //основной листинг==============================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            val cornerDp = resources.getDimension(R.dimen.track_poster_corner)
            val cornerPx = dimensionsFloatToIntConvert(cornerDp, this)
            Glide.with(this)
                .load(bundle.getString(Constants.PIC_URL_KEY))
                .placeholder(R.drawable.placeholder_large)
                .transform(RoundedCorners(cornerPx))
                .into(binding.playerTrackImage)
        }

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
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }

    private fun renderPlayerState(state: PlayerActivityState) {
        trackPlayButtonActivate(state.playerActivityPlayerReadiness)
        renderTrackPlayButton(state.playerActivityPlayerStatus)
        setTrackProgress(state.playerActivityTrackProgress)
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

}