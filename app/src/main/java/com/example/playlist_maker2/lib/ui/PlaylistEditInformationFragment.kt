package com.example.playlist_maker2.lib.ui

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlist_maker2.R
import com.example.playlist_maker2.databinding.FragmentInformationEditPlaylistBinding
import com.example.playlist_maker2.lib.domain.models.PlaylistInfoState
import com.example.playlist_maker2.main.ui.RootViewModel
import com.example.playlist_maker2.utils.constants.Constants
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class PlaylistEditInformationFragment() : Fragment() {

    private lateinit var binding: FragmentInformationEditPlaylistBinding

    private var nameIsLoaded = false

    private var inputUri: Uri? = null

    private val requester = PermissionRequester.instance()

    private var playlistName = ""

    private val playlistEditInformationViewModel: PlaylistEditInformationViewModel by viewModel()
    private val rootViewModel: RootViewModel by activityViewModel()

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.editPlaylistPicture.setImageURI(uri)
            inputUri = uri
        } else {
            Toast.makeText(requireContext(), Constants.PICTURE_NOT_TAKEN, Toast.LENGTH_SHORT)
        }
    }

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

        playlistEditInformationViewModel.observePlaylistDuplicateToast().observe(viewLifecycleOwner) {
            showPlaylistDuplicateToast(it)
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

            //пришлось переопределить из-за передачи оповещения об изменении названия плейлиста
            requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
                savedStateHandle?.set(Constants.PLAYLIST_NAME_KEY, playlistName)

                findNavController().navigateUp()
            }
        })

        //слушатели=================================================================================
        binding.editPlaylistName.addTextChangedListener(nameTextWatcher)

        playlistName = requireArguments().getString(Constants.PLAYLIST_NAME_KEY)!!
        playlistEditInformationViewModel.showInformation(playlistName!!)

        binding.editPlaylistSaveButton.setOnClickListener {
            val newName = letterCorrector(binding.editPlaylistName.text.toString())
            val newAbout = binding.editPlaylistAbout.text.toString()
            playlistEditInformationViewModel.setPlaylistInformation(
                playlistName,
                newName,
                newAbout,
                onPlaylistEdited = { result ->
                    if (result) {
                        val isEdited = (playlistName != newName)
                        //rootViewModel.setPlaylistEditedResult(isEdited, newName)
                        if (isEdited) {
                            changeImageName(playlistName, newName)
                        }

                        if (inputUri != null) {
                            saveImageToPrivateStorage(inputUri!!, newName)
                        }

                        val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
                        savedStateHandle?.set(Constants.PLAYLIST_NAME_KEY, newName)

                        findNavController().navigateUp()
                    }
                }
            )
        }

        binding.editPlaylistBackButton.setOnClickListener {
            val savedStateHandle = findNavController().previousBackStackEntry?.savedStateHandle
            savedStateHandle?.set(Constants.PLAYLIST_NAME_KEY, playlistName)

            findNavController().navigateUp()
        }

        binding.editPlaylistPicture.setOnClickListener {
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

    private fun changeImageName(
        oldPlaylistName: String,
        newPlaylistName: String
    ) {

        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")

        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }

        //создаём экземпляры класса File, который указывает на файл внутри каталога
        val oldFile = File(filePath, oldPlaylistName+".jpg")
        val newFile = File(filePath, newPlaylistName+".jpg")

        try {
            // создаём входящий и исходящий потоки байтов для передачи между старым и новым файлами
            val inputStream = FileInputStream(oldFile)
            val outputStream = FileOutputStream(newFile)
            // записываем картинку с помощью BitmapFactory
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

            //удаляем файл со старым названием
            if (oldFile.exists() && oldFile.isFile) {
                oldFile.delete()
            }
        } catch (e: Exception) {}

    }

    private fun letterCorrector(inputString: String): String {
        var outputString = inputString[0].uppercaseChar().toString()
        for (i in 1..inputString.length - 1) {
            outputString += inputString[i].lowercaseChar()
        }
        return outputString
    }

    private fun showPlaylistDuplicateToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun saveImageToPrivateStorage(
        uri: Uri,
        playlistName: String
    ) {
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = requireActivity().contentResolver.openInputStream(uri)

        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_album")
        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, playlistName+".jpg")
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

}