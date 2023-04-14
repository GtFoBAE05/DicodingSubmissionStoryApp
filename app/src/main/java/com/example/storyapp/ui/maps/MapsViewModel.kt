package com.example.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.remote.response.AllStoriesResponse
import com.example.storyapp.utils.Event
import com.example.storyapp.utils.Resource
import kotlinx.coroutines.launch

class MapsViewModel(private val userRepository: UserRepository) : ViewModel() {
    private var _token = MutableLiveData<Event<String>>()
    val token: LiveData<Event<String>> = _token

    private var _listStory = MutableLiveData<Resource<AllStoriesResponse>>()
    val listStory: LiveData<Resource<AllStoriesResponse>> = _listStory

    init {
        getToken()
    }

    fun getStoryWithMaps(tokenn: String) {
        viewModelScope.launch {
            userRepository.getStoryWithMaps("Bearer $tokenn").collect {
                _listStory.postValue(it)
            }
        }
    }

    private fun getToken() {
        viewModelScope.launch {
            userRepository.getToken().collect {
                _token.value = Event(it)
            }
        }
    }
}