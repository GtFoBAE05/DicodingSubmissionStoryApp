package com.example.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.remote.response.LoginResponse
import com.example.storyapp.utils.Event
import com.example.storyapp.utils.Resource
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _userLogin = MutableLiveData<Event<Resource<LoginResponse>>>()
    val userLogin: LiveData<Event<Resource<LoginResponse>>> = _userLogin

    fun login(email: String, password: String) {
        viewModelScope.launch {
            userRepository.login(email, password).collect {
                _userLogin.postValue(Event(it))
            }
        }
    }

    fun saveId(id: String) {
        viewModelScope.launch {
            userRepository.saveId(id)
        }
    }

    fun saveName(name: String) {
        viewModelScope.launch {
            userRepository.saveName(name)
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            userRepository.saveToken(token)
        }
    }

}