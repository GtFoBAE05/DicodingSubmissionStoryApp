package com.example.storyapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.remote.response.BasicResponse
import com.example.storyapp.utils.Event
import com.example.storyapp.utils.Resource
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: UserRepository):ViewModel() {

    private var _registerResponse = MutableLiveData<Event<Resource<BasicResponse>>>()
    val registerResponse: LiveData<Event<Resource<BasicResponse>>> = _registerResponse

    fun register(name:String, email:String, password:String) {
        viewModelScope.launch {
            userRepository.register(name, email, password).collect{
                _registerResponse.postValue(Event(it))
            }
        }
    }

}