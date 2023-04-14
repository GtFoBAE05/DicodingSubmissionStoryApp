package com.example.storyapp.ui.story.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.remote.response.DetailStoryResponse
import com.example.storyapp.utils.Resource
import kotlinx.coroutines.launch

class DetailStoryViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    private var _detailStory = MutableLiveData<Resource<DetailStoryResponse>>()
    val detailStory: LiveData<Resource<DetailStoryResponse>> = _detailStory

    init {
        getToken()
    }

    fun getDetailStory(id: String) {
        viewModelScope.launch {
            userRepository.getDetailStory("Bearer " + token.value, id).collect {
                _detailStory.postValue(it)
            }
        }
    }

    private fun getToken() {
        viewModelScope.launch {
            userRepository.getToken().collect {
                _token.value = it
            }
        }
    }
}