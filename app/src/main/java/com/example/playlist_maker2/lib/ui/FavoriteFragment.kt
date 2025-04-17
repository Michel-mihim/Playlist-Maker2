package com.example.playlist_maker2.lib.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlist_maker2.R
import com.example.playlist_maker2.databinding.FragmentFavouriteBinding
import com.example.playlist_maker2.search.domain.models.Track
import com.example.playlist_maker2.adapters.TracksAdapter
import com.example.playlist_maker2.player.domain.models.DBActivityState
import com.example.playlist_maker2.utils.constants.Constants.CLICK_DEBOUNCE_DELAY
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment: Fragment() {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private var isClickAllowed = true

    private lateinit var binding: FragmentFavouriteBinding

    private val favoriteViewModel: FavoriteViewModel by viewModel()

    private val adapter = TracksAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteViewModel.observeFavoriteTracksState().observe(viewLifecycleOwner) {
            showFavorite(it)
        }

        favoriteViewModel.observePlayerActivityIntent().observe(viewLifecycleOwner) { intent ->
            startActivity(intent)
        }

        binding.libEmptyText.text = getString(R.string.lib_empty)

        binding.favoriteTracksRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        //слушатели=================================================================================

        adapter.onItemClickListener = { track ->
            if (clickDebouncer()) {
                //запуск плеера
                favoriteViewModel.getPlayerIntent(track)
            }
        }

    }

    override fun onResume() {
        super.onResume()

        favoriteViewModel.showFavorite()
    }

    private fun showFavorite(state: DBActivityState) {
        when (state) {
            is DBActivityState.Empty -> {showEmpty()}
            is DBActivityState.Content -> {showContent(state.tracks)}
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

    private fun showContent(tracks: List<Track>) {
        contentViewsShow()

        binding.favoriteTracksRecycler.adapter = adapter
        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun showEmpty() {
        emptyViewsShow()
    }

    private fun emptyViewsShow() {
        binding.favoriteTracksRecycler.visibility = View.INVISIBLE
        binding.libEpmtyPic.visibility = View.VISIBLE
        binding.libEmptyText.visibility = View.VISIBLE
    }

    private fun contentViewsShow() {
        binding.favoriteTracksRecycler.visibility = View.VISIBLE
        binding.libEpmtyPic.visibility = View.INVISIBLE
        binding.libEmptyText.visibility = View.INVISIBLE
    }
}