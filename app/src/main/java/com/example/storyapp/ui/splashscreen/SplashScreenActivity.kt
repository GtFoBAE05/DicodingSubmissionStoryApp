package com.example.storyapp.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.storyapp.R
import com.example.storyapp.ui.home.HomeActivity
import com.example.storyapp.ui.login.MainActivity
import org.koin.android.ext.android.inject

class SplashScreenActivity : AppCompatActivity() {

    private val splashScreenViewModel: SplashScreenViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)

        splashScreen.setKeepOnScreenCondition { true }

        setupObserver()

    }

    private fun setupObserver() {
        splashScreenViewModel.getToken().observe(this) {
            if (it.isNullOrEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }
}