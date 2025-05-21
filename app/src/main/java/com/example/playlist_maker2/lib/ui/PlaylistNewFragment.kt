package com.example.playlist_maker2.lib.ui

import android.Manifest
import android.app.Activity
import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlist_maker2.R
import com.example.playlist_maker2.databinding.FragmentNewPlaylistBinding
import com.example.playlist_maker2.lib.domain.api.PlaylistsInteractor
import com.example.playlist_maker2.lib.domain.models.Playlist
import com.example.playlist_maker2.player.domain.NewPlaylistNameLoadNotifier
import com.example.playlist_maker2.player.domain.models.DBActivityState
import com.example.playlist_maker2.player.domain.models.DBPlaylistsState
import com.example.playlist_maker2.search.domain.models.Track
import com.example.playlist_maker2.utils.constants.Constants
import com.example.playlist_maker2.utils.constants.Constants.CLICK_DEBOUNCE_DELAY
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class PlaylistNewFragment: Fragment(), NewPlaylistNameLoadNotifier {

    private lateinit var binding:  FragmentNewPlaylistBinding

    private var inputUri: Uri? = null

    private var isClickAllowed = true

    private lateinit var activity: Activity

    private val requester = PermissionRequester.instance()

    private var picIsLoaded = false
    private var nameIsLoaded = false
    private var aboutIsLoaded = false

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.newPlaylistPicture.setImageURI(uri)
            inputUri = uri
        } else {
            Toast.makeText(requireContext(), "Изображение не выбрано", Toast.LENGTH_SHORT)
        }
    }

    private val playlistNewViewModel: PlaylistNewViewModel by viewModel()

    override fun loadUpdate(isLoaded: Boolean) {}

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

        playlistNewViewModel.observePlaylistFragmentToastState().observe(viewLifecycleOwner) {
            showToast(it)
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNeutralButton("Отмена") { dialog, which ->
                // ничего не делаем
            }.setNegativeButton("Завершить") { dialog, which ->
                // выходим из окна без сохранения
                try {//костыль чтобы не проверять каким образом вызван фрагмент
                    findNavController().navigateUp()
                } catch (e: Exception) {//если вход через bottomSheet
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        //==========================================================================================
        val nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nameIsLoaded = !stringIsEmpty(s)
                binding.createNewPlaylistButton.isEnabled = nameIsLoaded
                if (nameIsLoaded) {
                    binding.createNewPlaylistButton.setBackgroundColor(requireContext().getColor(R.color.new_playlist_button_active_color))
                    binding.newPlaylistName.background = requireContext().getDrawable(R.drawable.rounded_edittext2_active)
                    binding.newPlaylistAbout.background = requireContext().getDrawable(R.drawable.rounded_edittext2_active)
                    binding.newPlaylistUpperHintName.visibility = View.VISIBLE
                    binding.newPlaylistUpperHintAbout.visibility = View.VISIBLE

                    try {
                        (activity as NewPlaylistNameLoadNotifier).loadUpdate(true)
                    } catch (e: Exception) {}

                } else {
                    binding.createNewPlaylistButton.setBackgroundColor(requireContext().getColor(R.color.new_playlist_button_inactive_color))
                    binding.newPlaylistName.background = requireContext().getDrawable(R.drawable.rounded_edittext2)
                    binding.newPlaylistAbout.background = requireContext().getDrawable(R.drawable.rounded_edittext2)
                    binding.newPlaylistUpperHintName.visibility = View.GONE
                    binding.newPlaylistUpperHintAbout.visibility = View.GONE

                    try {
                        (activity as NewPlaylistNameLoadNotifier).loadUpdate(false)
                    } catch (e: Exception) {}


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
        binding.newPlaylistBackButton.setOnClickListener {//СВОЯ КНОПКА НАЗАД
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

        //==========================================================================================
        binding.createNewPlaylistButton.setOnClickListener {
            if (clickDebouncer()) {
                val playlistName = binding.newPlaylistName.text.toString()
                val playlistAbout = binding.newPlaylistAbout.text.toString()

                if (inputUri != null) {
                    saveImageToPrivateStorage(inputUri!!, playlistName)
                }

                playlistNewViewModel.onAddPlaylistButtonClicked(Playlist(playlistName, playlistAbout, null, null))
            }
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity = context as Activity

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                confirmationDialogManager()
            }
        })
    }

    override fun onDetach() {
        super.onDetach()

        picIsLoaded = false
        nameIsLoaded = false
        aboutIsLoaded = false

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {}
        })

    }

    private fun confirmationDialogManager() {
        if (nameIsLoaded || picIsLoaded || aboutIsLoaded) {
            confirmDialog.show()
        } else try {//костыль чтобы не проверять каким образом вызван фрагмент
            findNavController().navigateUp()
        } catch (e: Exception) {//если вход через bottomSheet
            requireActivity().supportFragmentManager.popBackStack()
        }

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

    private fun stringIsEmpty(s: CharSequence?): Boolean {
        return if (s.isNullOrEmpty()) {
            true
        } else {
            false
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

    private fun showToast(message: String) {
        try {//костыль чтобы не проверять каким образом вызван фрагмент
            findNavController().navigateUp()
        } catch (e: Exception) {//если вход через bottomSheet
            requireActivity().supportFragmentManager.popBackStack()
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

}
