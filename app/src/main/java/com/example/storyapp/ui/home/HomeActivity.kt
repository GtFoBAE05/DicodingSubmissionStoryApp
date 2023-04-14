package com.example.storyapp.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navView = binding.bottomNavigationView

        val navController = findNavController(R.id.fragmentContainerView)

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.StoryMenu, R.id.AddStoryMenu, R.id.MapsMenu, R.id.ProfileMenu, R.id.DetailStoryMenu
        ).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //hide bot nav bar di detail story
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.DetailStoryMenu -> {
                    navView.visibility = View.GONE
                }
                else -> {
                    navView.visibility = View.VISIBLE
                }
            }
        }

    }


}