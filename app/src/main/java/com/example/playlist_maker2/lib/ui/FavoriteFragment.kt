package com.example.playlist_maker2.lib.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlist_maker2.R
import com.example.playlist_maker2.databinding.FragmentFavouriteBinding
import com.example.playlist_maker2.search.domain.models.Track
import com.example.playlist_maker2.adapters.TracksAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment: Fragment() {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

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

        favoriteViewModel.observeFavoriteTracks().observe(viewLifecycleOwner) {
            showFavorite(it)
        }

        binding.libEmptyText.text = getString(R.string.lib_empty)

        binding.favoriteTracksRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        favoriteViewModel.showFavorite()
    }

    private fun showFavorite(tracks: List<Track>) {
        //contentViewsShow()

        binding.favoriteTracksRecycler.adapter = adapter
        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()
    }
}