package com.example.playlist_maker2.lib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlist_maker2.databinding.FragmentEditPlaylistBinding

class PlaylistEditFragment : Fragment() {

    private lateinit var binding: FragmentEditPlaylistBinding

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

        binding.playlistEditBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.playlistEditName.text = requireArguments().getString("name")
        binding.playlistEditAbout.text = requireArguments().getString("about")
        binding.playlistEditTracksCount.text = requireArguments().getInt("tracks_count").toString() +
                wordModifier(requireArguments().getInt("tracks_count"))

    }

    private fun wordModifier(tracksCount: Int?): String {
        var word = ""
        var preLastChar: Char? = null
        var lastChar: Char? = null

        lastChar = tracksCount.toString().last()
        if (tracksCount.toString().length >= 2) {
            preLastChar = tracksCount.toString()[tracksCount.toString().length - 2]
        }

        when (preLastChar) {
            '1' -> {
                word = " треков"
            }

            else -> {
                when (lastChar) {
                    '1' -> word = " трек"
                    '2' -> word = " трека"
                    '3' -> word = " трека"
                    '4' -> word = " трека"
                    else -> word = " треков"
                }
            }
        }
        return word
    }
}