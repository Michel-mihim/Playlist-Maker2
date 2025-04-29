package com.example.playlist_maker2.lib.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlist_maker2.databinding.FragmentNewPlaylistBinding
import com.example.playlist_maker2.utils.constants.Constants
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch

class PlaylistNewFragment: Fragment() {

    private lateinit var binding:  FragmentNewPlaylistBinding

    private val requester = PermissionRequester.instance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newPlaylistBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.newPlaylistPicture.setOnClickListener {
            lifecycleScope.launch {
                requester.request(Manifest.permission.READ_MEDIA_IMAGES).collect { result ->
                    when (result) {
                        is PermissionResult.Granted -> {

                        }

                        else -> {
                            Toast.makeText(requireContext(), Constants.READ_MEDIA_IMAGES_DENIED,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
