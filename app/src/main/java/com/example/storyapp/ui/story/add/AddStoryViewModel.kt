package com.example.storyapp.ui.story.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.remote.response.BasicResponse
import com.example.storyapp.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    private var _addResponse = MutableLiveData<Resource<BasicResponse>>()
    val addResponse: LiveData<Resource<BasicResponse>> = _addResponse

    init {
        getToken()
    }

    fun addNewStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ) {
        viewModelScope.launch {
            userRepository.addNewStory("Bearer " + token.value, file, description, lat, lon)
                .collect {
                    _addResponse.postValue(it)
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