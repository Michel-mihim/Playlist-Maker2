package com.example.playlist_maker2.lib.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import com.example.playlist_maker2.R
import com.example.playlist_maker2.databinding.ActivityRootBinding
import com.example.playlist_maker2.databinding.FragmentPlaylistBinding
import com.example.playlist_maker2.main.ui.RootActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {

    companion object {
        fun newInstance() = PlaylistFragment()
    }

    private lateinit var binding: FragmentPlaylistBinding

    private val playlistViewModel: PlaylistViewModel by viewModel()

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
        binding.playlistEmptyText.text = getString(R.string.no_playlist)

        binding.addPlaylistButton.setOnClickListener {

            findNavController().navigate(R.id.action_libFragment_to_playlistNewFragment)
        }
    }
}