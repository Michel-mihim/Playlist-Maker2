package com.example.playlist_maker2.lib.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlist_maker2.R
import com.example.playlist_maker2.adapters.PlaylistTracksAdapter
import com.example.playlist_maker2.databinding.FragmentEditPlaylistBinding
import com.example.playlist_maker2.lib.domain.models.PlaylistEditState
import com.example.playlist_maker2.lib.domain.models.PlaylistTrack
import com.example.playlist_maker2.player.data.converters.TrackPlaylistTrackConvertor
import com.example.playlist_maker2.player.data.db.entities.PlaylistTrackEntity
import com.example.playlist_maker2.search.domain.models.Track
import com.example.playlist_maker2.utils.constants.Constants.CLICK_DEBOUNCE_DELAY
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistEditFragment : Fragment() {

    private lateinit var binding: FragmentEditPlaylistBinding

    private var isClickAllowed = true

    private val playlistEditViewModel: PlaylistEditViewModel by viewModel()

    private val adapter = PlaylistTracksAdapter()

    private val trackPlaylistTrackConvertor = TrackPlaylistTrackConvertor()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistEditViewModel.observePlaylistEditState().observe(viewLifecycleOwner) {
            showContent(it)
        }

        playlistEditViewModel.observePlayerActivityIntent().observe(viewLifecycleOwner) { intent ->
            startActivity(intent)
        }

        val playlistEditName = requireArguments().getString("name")
        binding.playlistEditName.text = playlistEditName
        binding.playlistEditAbout.text = requireArguments().getString("about")

        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")
        val file = File(filePath, playlistEditName+".jpg")
        if (file.exists()) {
            binding.playlistEditImage.setImageURI(file.toUri())
        } else {
            binding.playlistEditImage.setImageDrawable(requireContext().getDrawable(R.drawable.placeholder_large))
        }

        binding.bottomPlaylistEditRecycler.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)

        playlistEditViewModel.showContent(playlistEditName!!)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistEditBottomSheet)

        adapter.onItemClickListener = { track ->
            if (clickDebouncer()) {
                playlistEditViewModel.getPlayerIntent(convertToTrack(track))
            }
        }

        binding.playlistEditBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun showContent(state: PlaylistEditState) {
        binding.bottomPlaylistEditRecycler.adapter = adapter

        if (state is PlaylistEditState.Content) {
            val tracks = state.tracks
            adapter.tracks.clear()
            adapter.tracks.addAll(tracks)
            adapter.notifyDataSetChanged()

            binding.playlistEditTracksDuration.text = state.tracksDurationString

            binding.playlistEditTracksCount.text = state.tracksCountString

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

            binding.playlistEditBottomSheet.visibility = when {
                tracks.isNotEmpty() -> View.VISIBLE
                else -> View.GONE
            }

        }

    }

    private fun clickDebouncer() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun convertToTrack(playlistTrack: PlaylistTrack): Track {
        return trackPlaylistTrackConvertor.map(playlistTrack)
    }

}