package com.example.playlist_maker2.lib.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlist_maker2.R
import com.example.playlist_maker2.adapters.PlaylistsAdapter
import com.example.playlist_maker2.databinding.ActivityRootBinding
import com.example.playlist_maker2.databinding.FragmentPlaylistBinding
import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.main.ui.RootActivity
import com.example.playlist_maker2.player.domain.models.DBPlaylistsState
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {

    companion object {
        fun newInstance() = PlaylistFragment()
    }

    private lateinit var binding: FragmentPlaylistBinding

    private val playlistViewModel: PlaylistViewModel by viewModel()

    private lateinit var adapter: PlaylistsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistViewModel.observePlaylistsState().observe(viewLifecycleOwner) {
            showPlaylists(it)
        }

        binding.playlistEmptyText.text = getString(R.string.no_playlist)

        binding.playlistsRecycler.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.addPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.action_libFragment_to_playlistNewFragment)
        }
    }

    override fun onStart() {
        super.onStart()

        playlistViewModel.showPlaylists()
        contentViewsShow()
    }

    private fun showPlaylists(state: DBPlaylistsState) {
        when (state) {
            is DBPlaylistsState.Empty -> {showEmpty()}
            is DBPlaylistsState.Content -> {showContent(state.playlists)}
        }
    }

    private fun showEmpty() {
        emptyViewsShow()
    }

    private fun showContent(playlists: List<Playlist>) {
        contentViewsShow()

        adapter = PlaylistsAdapter(requireContext())

        binding.playlistsRecycler.adapter = adapter
        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        adapter.notifyDataSetChanged()
    }

    private fun emptyViewsShow() {
        binding.playlistsRecycler.visibility = View.GONE
        binding.playlistEpmtyPic.visibility = View.VISIBLE
        binding.playlistEmptyText.visibility = View.VISIBLE
    }

    private fun contentViewsShow() {
        binding.playlistsRecycler.visibility = View.VISIBLE
        binding.playlistEpmtyPic.visibility = View.INVISIBLE
        binding.playlistEmptyText.visibility = View.INVISIBLE
    }

}