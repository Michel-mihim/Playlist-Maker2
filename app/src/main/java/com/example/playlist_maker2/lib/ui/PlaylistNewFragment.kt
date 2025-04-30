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
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.graphics.alpha
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlist_maker2.R
import com.example.playlist_maker2.databinding.FragmentNewPlaylistBinding
import com.example.playlist_maker2.utils.constants.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.nio.file.WatchEvent

class PlaylistNewFragment: Fragment() {

    private lateinit var binding:  FragmentNewPlaylistBinding

    private val requester = PermissionRequester.instance()

    private var picIsLoaded = false
    private var nameIsLoaded = false
    private var aboutIsLoaded = false

    lateinit var confirmDialog: MaterialAlertDialogBuilder

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

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which ->
                // ничего не делаем
            }.setNegativeButton("Завершить") { dialog, which ->
                // выходим из окна без сохранения
                findNavController().navigateUp()
            }
        //==========================================================================================
        val nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nameIsLoaded = !stringIsEmpty(s)
                binding.createNewPlaylistButton.isEnabled = nameIsLoaded
                if (nameIsLoaded) {
                    binding.createNewPlaylistButton.setBackgroundColor(requireContext().getColor(R.color.new_playlist_button_active_color))
                } else {
                    binding.createNewPlaylistButton.setBackgroundColor(requireContext().getColor(R.color.new_playlist_button_inactive_color))
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.newPlaylistName.addTextChangedListener(nameTextWatcher)

        //==========================================================================================
        val aboutTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                aboutIsLoaded = !stringIsEmpty(s)
                binding.createNewPlaylistButton.isEnabled = nameIsLoaded
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.newPlaylistAbout.addTextChangedListener(aboutTextWatcher)

        //==========================================================================================
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")
        val file = File(filePath, "first_cover.jpg")

        binding.newPlaylistPicture.setImageURI(file.toUri())
        binding.newPlaylistPicture.cropToPadding = true

        //==========================================================================================
        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                confirmationDialogManager()
            }
        })

        //==========================================================================================
        binding.newPlaylistBackButton.setOnClickListener {
            confirmationDialogManager()
        }

        binding.newPlaylistPicture.setOnClickListener {
            lifecycleScope.launch {
                requester.request(Manifest.permission.READ_MEDIA_IMAGES).collect { result ->
                    when (result) {
                        is PermissionResult.Granted -> {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            picIsLoaded = true
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

    private fun confirmationDialogManager() {
        if (nameIsLoaded || picIsLoaded || aboutIsLoaded) {
            confirmDialog.show()
        } else findNavController().navigateUp()
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

    private fun stringIsEmpty(s: CharSequence?): Boolean {
        return if (s.isNullOrEmpty()) {
            true
        } else {
            false
        }
    }

}
