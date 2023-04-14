package com.example.storyapp.ui.story.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.local.entity.StoryItem
import com.example.storyapp.utils.Event
import kotlinx.coroutines.launch

class ListStoryViewModel(private val userRepository: UserRepository) : ViewModel() {

    private var _token = MutableLiveData<Event<String>>()
    val token: LiveData<Event<String>> = _token

    private var _listStory = MutableLiveData<PagingData<StoryItem>>()
    val listStory: LiveData<PagingData<StoryItem>> = _listStory


    init {
        getToken()
    }


    fun getAllStory(tokenn: String) {
        viewModelScope.launch {
            userRepository.getAllStory("Bearer $tokenn").cachedIn(viewModelScope).collect {
                _listStory.value = it
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