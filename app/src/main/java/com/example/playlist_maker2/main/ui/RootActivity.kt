package com.example.playlist_maker2.main.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlist_maker2.R
import com.example.playlist_maker2.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    private lateinit var destination: NavDestination

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            //управляем видимостью нижней панели
            val shouldBottomNavigationHide = when (destination.id) {
                R.id.playlistNewFragment -> {
                    false
                }
                R.id.playlistEditFragment -> {
                    false
                }
                R.id.playlistEditInformationFragment -> {
                    false
                }
                else -> {
                    true
                }
            }
            setBottomNavigationViewVisibility(shouldBottomNavigationHide)

            this.destination = destination
        }
    }

    private fun setBottomNavigationViewVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.bottomNavigationView.visibility = View.VISIBLE
        } else {
            binding.bottomNavigationView.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        //ждем момента чтобы переопределить обратно нажатие системной кнопки НАЗАД
        when (destination.id)  {
            R.id.libFragment -> {
                onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        finish()
                    }
                })
            }

            //тут мы вляпались в переопределение системной кнопки НАЗАД ради диалога подтверждения
            R.id.playlistNewFragment -> {}

            R.id.playlistEditInformationFragment -> {}

            else -> {
                onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        supportFragmentManager.popBackStack()
                    }
                })
            }
        }

        super.onBackPressed()
    }

}