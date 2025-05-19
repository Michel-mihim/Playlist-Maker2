package com.example.playlist_maker2.lib.ui

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlist_maker2.R
import com.example.playlist_maker2.adapters.PlaylistsAdapter
import com.example.playlist_maker2.adapters.TracksAdapter
import com.example.playlist_maker2.databinding.FragmentEditPlaylistBinding
import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.search.domain.models.Track
import java.io.File

class PlaylistEditFragment : Fragment() {

    private lateinit var binding: FragmentEditPlaylistBinding

    private lateinit var adapter: TracksAdapter

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

        val playlistEditName = requireArguments().getString("name")
        binding.playlistEditName.text = playlistEditName
        binding.playlistEditAbout.text = requireArguments().getString("about")
        /*
        binding.playlistEditTracksCount.text = requireArguments().getInt("tracks_count").toString() +
                wordModifier(requireArguments().getInt("tracks_count"))
        binding.playlistEditTracksDuration.text = (requireArguments().getInt("tracks_duration") / 1000).toString() +
                wordMinuteModifier(requireArguments().getInt("tracks_duration") / 1000)
         */

        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")
        val file = File(filePath, playlistEditName+".jpg")

        if (file.exists()) {
            binding.playlistEditImage.setImageURI(file.toUri())
        } else {
            binding.playlistEditImage.setImageDrawable(requireContext().getDrawable(R.drawable.placeholder_large))
        }

        binding.bottomPlaylistEditRecycler.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)

        //showContent()

    }

    private fun showContent(tracks: List<Track>) {
        binding.bottomPlaylistEditRecycler.adapter = adapter
        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()

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

    private fun wordMinuteModifier(duration: Int?): String {
        var word = ""
        var preLastChar: Char? = null
        var lastChar: Char? = null

        lastChar = duration.toString().last()
        if (duration.toString().length >= 2) {
            preLastChar = duration.toString()[duration.toString().length - 2]
        }

        when (preLastChar) {
            '1' -> {
                word = " минут"
            }

            else -> {
                when (lastChar) {
                    '1' -> word = " минута"
                    '2' -> word = " минуты"
                    '3' -> word = " минуты"
                    '4' -> word = " минуты"
                    else -> word = " минут"
                }
            }
        }
        return word
    }

}