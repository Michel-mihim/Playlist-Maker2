package com.example.playlist_maker2.lib.ui

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.example.playlist_maker2.search.domain.models.Track
import com.example.playlist_maker2.utils.constants.Constants
import com.example.playlist_maker2.utils.constants.Constants.CLICK_DEBOUNCE_DELAY
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistEditFragment : Fragment() {

    private lateinit var binding: FragmentEditPlaylistBinding

    private var isClickAllowed = true

    private val playlistEditViewModel: PlaylistEditViewModel by viewModel()

    private val adapter = PlaylistTracksAdapter()

    private var isPlaylistEmpty = true

    private var playlistEditName = ""
    private var playlistEditAbout = ""
    private var playlistEditTracksCount = 0
    private var tracks = emptyList<PlaylistTrack>()
    private var currentTracksCountString = ""

    private val trackPlaylistTrackConvertor = TrackPlaylistTrackConvertor()

    private lateinit var bottomSheetBehaviorTracksRecycler: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorMenu: BottomSheetBehavior<LinearLayout>

    private lateinit var trackDeleteConfirmDialog: MaterialAlertDialogBuilder
    private lateinit var playlistDeleteConfirmDialog: MaterialAlertDialogBuilder

    private var currentTrackId: String? = null
    private var currentPlaylistName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistEditViewModel.observePlaylistEditState().observe(viewLifecycleOwner) {
            showContent(it)
        }

        playlistEditViewModel.observePlayerActivityIntent().observe(viewLifecycleOwner) { intent ->
            startActivity(intent)
        }

        playlistEditViewModel.observeShareActivityIntentLiveData().observe(viewLifecycleOwner) { intent ->
            startActivity(intent)
        }

        playlistEditViewModel.observePlaylistDeletedNotifier().observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        trackDeleteConfirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить трек")
            .setMessage("Вы уверены, что хотите удалить трек из плейлиста?")
            .setNeutralButton("Отмена") { dialog, which ->
                // ничего не делаем
            }.setNegativeButton("Удалить") { dialog, which ->
                playlistEditViewModel.deletePlaylistTrack(currentTrackId!!, currentPlaylistName!!)
            }

        //тут заводим playlistName, вокруг которого всё и будет крутиться
        playlistEditName = requireArguments().getString(Constants.PLAYLIST_NAME_KEY)!!
        binding.playlistEditName.text = playlistEditName

        playlistEditViewModel.showContent(playlistEditName!!)

        playlistDeleteConfirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить плейлист")
            .setMessage("Хотите удалить плейлист?")
            .setNeutralButton("Нет") { dialog, which ->
                // ничего не делаем
            }.setNegativeButton("Да") { dialog, which ->
                playlistEditViewModel.deletePlaylist(playlistEditName)
            }

        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")
        val file = File(filePath, playlistEditName+".jpg")
        if (file.exists()) {
            binding.playlistEditImage.setImageURI(file.toUri())
        } else {
            binding.playlistEditImage.setImageDrawable(requireContext().getDrawable(R.drawable.placeholder_large))
        }

        binding.bottomPlaylistEditRecycler.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL, false)

        bottomSheetBehaviorTracksRecycler = BottomSheetBehavior.from(binding.playlistEditBottomSheet)
        bottomSheetBehaviorMenu = BottomSheetBehavior.from(binding.playlistEditMenuBottomSheet)
        bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehaviorMenu.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            @RequiresApi(Build.VERSION_CODES.O)
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
                binding.menuOverlay.alpha = (slideOffset + 1) / 2
            }

        })

        binding.menuOverlay.setOnClickListener {
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        //НАЖАТИЕ НА ТРЕК В СПИСКЕ==================================================================
        adapter.onItemClickListener = { track ->
            if (clickDebouncer()) {
                playlistEditViewModel.getPlayerIntent(convertToTrack(track))
            }
        }

        adapter.onItemLongClickListener = { track ->
            currentTrackId = track.trackId
            currentPlaylistName = track.playlistName
            trackDeleteConfirmDialog.show()
        }

        binding.playlistEditBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        //НАЖАТИЕ НА ОТДЕЛЬНЫЕ КВАДРАТНЫЕ КНОПКИ====================================================
        binding.playlistEditShare.setOnClickListener {
            if (isPlaylistEmpty) {
                Toast.makeText(
                    requireContext(),
                    "В этом плейлисте нет списка треков, которым можно поделиться",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                playlistEditViewModel.sharePlaylist(myPlaylist())
            }

        }

        binding.playlistEditMenu.setOnClickListener {
            if (clickDebouncer()) {
                binding.bottomTrackName.text = playlistEditName
                binding.bottomTracksCount.text = currentTracksCountString

                val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")
                val file = File(filePath, playlistEditName+".jpg")

                if (file.exists()) {
                    binding.bottomTrackImage.setImageURI(file.toUri())
                } else {
                    binding.bottomTrackImage.setImageDrawable(requireContext().getDrawable(R.drawable.placeholder_large))
                }

                bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        //НАЖАТИЕ НА ПУНКТЫ МЕНЮ====================================================================
        binding.playlistEditBottomMenuShare.setOnClickListener {
            if (isPlaylistEmpty) {
                Toast.makeText(
                    requireContext(),
                    "В этом плейлисте нет списка треков, которым можно поделиться",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                playlistEditViewModel.sharePlaylist(myPlaylist())
            }
        }

        binding.playlistEditBottomMenuEditInformation.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.PLAYLIST_NAME_KEY, playlistEditName)

            findNavController().navigate(R.id.action_playlistEditFragment_to_playlistEditInformationFragment, bundle)
        }

        binding.playlistEditBottomMenuDelete.setOnClickListener {
            playlistDeleteConfirmDialog.show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun overlay(enabled: Boolean) {
        if (enabled) {
            binding.menuOverlay.apply {
                visibility = View.VISIBLE
                focusable = View.FOCUSABLE
                isEnabled = true
                isActivated = true
            }
        } else {
            binding.menuOverlay.apply {
                visibility = View.GONE
                focusable = View.NOT_FOCUSABLE
                isEnabled = false
                isActivated = false
            }
        }
    }

    private fun showContent(state: PlaylistEditState) {
        binding.bottomPlaylistEditRecycler.adapter = adapter

        if (state is PlaylistEditState.Content) {
            tracks = state.tracks
            adapter.tracks.clear()
            adapter.tracks.addAll(tracks)
            adapter.notifyDataSetChanged()

            binding.playlistEditAbout.text = state.playlistAbout

            binding.playlistEditTracksDuration.text = state.tracksDurationString

            binding.playlistEditTracksCount.text = state.tracksCountString
            currentTracksCountString = state.tracksCountString

            bottomSheetBehaviorTracksRecycler.state = BottomSheetBehavior.STATE_COLLAPSED

            binding.playlistEditBottomSheet.visibility = when {
                tracks.isNotEmpty() -> View.VISIBLE
                else -> View.GONE
            }

            if (tracks.isEmpty()) {
                isPlaylistEmpty = true
            } else {
                isPlaylistEmpty = false
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

    private fun myPlaylist(): String {
        var playlist = playlistEditName + "\n"
        playlist += playlistEditAbout + "\n"
        playlist += phraseTrackGenerator(playlistEditTracksCount) + "\n"

        var counter = 1
        for (track in tracks) {
            val item = counter.toString() +
                    ". " +
                    track.artistName +
                    " - " +
                    track.trackName +
                    " (" +
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis) +
                    ")" +
                    "\n"
            playlist += item
            counter += 1
        }
        return playlist
    }

    private fun phraseTrackGenerator(tracksCount: Int?): String {
        var word = ""
        var preLastChar: Char? = null
        var lastChar: Char? = null

        lastChar = tracksCount.toString().last()
        if (tracksCount.toString().length >= 2) {
            preLastChar = tracksCount.toString()[tracksCount.toString().length - 2]
        }

        when (preLastChar) {
            '1' -> {
                word = tracksCount.toString() + " треков"
            }

            else -> {
                when (lastChar) {
                    '1' -> word = tracksCount.toString() + " трек"
                    '2' -> word = tracksCount.toString() + " трека"
                    '3' -> word = tracksCount.toString() + " трека"
                    '4' -> word = tracksCount.toString() + " трека"
                    else -> word = tracksCount.toString() + " треков"
                }
            }
        }
        return word
    }

}