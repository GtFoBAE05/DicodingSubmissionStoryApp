package com.example.storyapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getId() = userRepository.getId().asLiveData()

    fun getName() = userRepository.getName().asLiveData()

    fun removeAll() {
        viewModelScope.launch {
            userRepository.removeAll()
        }
    }

}