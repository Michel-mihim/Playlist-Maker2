package com.example.playlist_maker2.lib.ui

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlist_maker2.databinding.FragmentNewPlaylistBinding
import com.example.playlist_maker2.utils.constants.Constants
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class PlaylistNewFragment: Fragment() {

    private lateinit var binding:  FragmentNewPlaylistBinding

    private val requester = PermissionRequester.instance()

    private var picIsLoaded = false
    private var nameIsLoaded = false
    private var aboutIsLoaded = false

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.newPlaylistPicture.setImageURI(uri)
            saveImageToPrivateStorage(uri)
        } else {
            Toast.makeText(requireContext(), "Изображение не выбрано", Toast.LENGTH_SHORT)
        }
    }

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
        //==========================================================================================
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.createNewPlaylistButton.isEnabled = newPlaylistCreateButtonIsEnabled(s)
                binding.createNewPlaylistButton.text = newPlaylistCreateButtonIsEnabled(s).toString()
                nameIsLoaded = newPlaylistCreateButtonIsEnabled(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.newPlaylistName.addTextChangedListener(textWatcher)

        //==========================================================================================
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")
        val file = File(filePath, "first_cover.jpg")
        binding.newPlaylistPicture.setImageURI(file.toUri())
        //==========================================================================================

        binding.newPlaylistBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.newPlaylistPicture.setOnClickListener {
            lifecycleScope.launch {
                requester.request(Manifest.permission.READ_MEDIA_IMAGES).collect { result ->
                    when (result) {
                        is PermissionResult.Granted -> {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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

    override fun onDetach() {
        super.onDetach()


    }

    private fun saveImageToPrivateStorage(uri: Uri) {
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")
        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "first_cover.jpg")
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    private fun newPlaylistCreateButtonIsEnabled(s: CharSequence?): Boolean {
        return if (s.isNullOrEmpty()) {
            false
        } else {
            true
        }
    }

}
