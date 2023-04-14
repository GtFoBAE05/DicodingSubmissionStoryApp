package com.example.storyapp.ui.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.UserRepository

class SplashScreenViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getToken() = userRepository.getToken().asLiveData()

}