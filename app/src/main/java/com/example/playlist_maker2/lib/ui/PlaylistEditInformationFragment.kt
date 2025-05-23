package com.example.playlist_maker2.lib.ui

import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlist_maker2.R
import com.example.playlist_maker2.databinding.FragmentInformationEditPlaylistBinding
import com.example.playlist_maker2.lib.domain.models.PlaylistInfoState
import com.example.playlist_maker2.utils.constants.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistEditInformationFragment() : Fragment() {

    private lateinit var binding: FragmentInformationEditPlaylistBinding

    private var nameIsLoaded = false

    private var playlistName = ""

    private val playlistEditInformationViewModel: PlaylistEditInformationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInformationEditPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistEditInformationViewModel.observePlaylistInfoState().observe(viewLifecycleOwner) {
            showContent(it)
        }

        //==========================================================================================
        val nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nameIsLoaded = !stringIsEmpty(s)
                binding.editPlaylistSaveButton.isEnabled = nameIsLoaded
                if (nameIsLoaded) {
                    binding.editPlaylistSaveButton.setBackgroundColor(requireContext().getColor(R.color.new_playlist_button_active_color))
                    binding.editPlaylistName.background = requireContext().getDrawable(R.drawable.rounded_edittext2_active)
                    binding.editPlaylistAbout.background = requireContext().getDrawable(R.drawable.rounded_edittext2_active)
                    binding.editPlaylistUpperHintName.visibility = View.VISIBLE
                    binding.editPlaylistUpperHintAbout.visibility = View.VISIBLE
                } else {
                    binding.editPlaylistSaveButton.setBackgroundColor(requireContext().getColor(R.color.new_playlist_button_inactive_color))
                    binding.editPlaylistName.background = requireContext().getDrawable(R.drawable.rounded_edittext2)
                    binding.editPlaylistAbout.background = requireContext().getDrawable(R.drawable.rounded_edittext2)
                    binding.editPlaylistUpperHintName.visibility = View.GONE
                    binding.editPlaylistUpperHintAbout.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.editPlaylistName.addTextChangedListener(nameTextWatcher)

        playlistName = requireArguments().getString(Constants.PLAYLIST_NAME_KEY)!!
        playlistEditInformationViewModel.showInformation(playlistName!!)

        binding.editPlaylistSaveButton.setOnClickListener {
            playlistEditInformationViewModel.setPlaylistInformation(
                playlistName,
                binding.editPlaylistName.text.toString(),
                binding.editPlaylistAbout.text.toString(),
                onPlaylistEdited = {

                    findNavController().navigateUp()
                }
            )
        }
    }

    private fun showContent(state: PlaylistInfoState) {
        if (state is PlaylistInfoState.Content) {
            binding.editPlaylistName.setText(state.playlistName)
            binding.editPlaylistAbout.setText(state.playlistAbout)

            val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")
            val file = File(filePath, state.playlistName+".jpg")

            if (file.exists()) {
                binding.editPlaylistPicture.setImageURI(file.toUri())
            } else {
                binding.editPlaylistPicture.setImageDrawable(requireContext().getDrawable(R.drawable.placeholder_large))
            }
        }

    }

    private fun stringIsEmpty(s: CharSequence?): Boolean {
        return if (s.isNullOrEmpty()) {
            true
        } else {
            false
        }
    }

}