package com.example.playlist_maker2.settings.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.playlist_maker2.databinding.FragmentSettingsBinding
import com.example.playlist_maker2.utils.constants.Constants.EMAIL_CLIENT_NOT_FOUND
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SettingsFragment: Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsViewModel.observeSettingsActivityTheme().observe(viewLifecycleOwner) { isDark ->
            darkThemeSwitcherActivated(isDark)
        }

        settingsViewModel.observeShareActivityIntentLiveData().observe(viewLifecycleOwner) {intent ->
            startActivity(intent)
        }

        settingsViewModel.observeSupportEmailActivityIntentLiveData().observe(viewLifecycleOwner) {intent ->
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), EMAIL_CLIENT_NOT_FOUND, Toast.LENGTH_LONG).show()
            }

        }

        settingsViewModel.observeTermsIntentLiveData().observe(viewLifecycleOwner) { intent ->
            startActivity(intent)
        }

        //слушатели нажатий=========================================================================
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.switchTheme(checked)
        }

        binding.buttonShare.setOnClickListener{
            settingsViewModel.shareApp()
        }

        binding.buttonSupport.setOnClickListener{
            settingsViewModel.openSupport()
        }

        binding.buttonLicense.setOnClickListener{
            settingsViewModel.openTerms()
        }
    }

    private fun darkThemeSwitcherActivated(isDark: Boolean) {
        binding.themeSwitcher.isChecked = isDark
    }

}