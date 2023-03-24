package com.jeflette.mystory.presentation.homeFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeflette.mystory.data.MyStoryRepository
import com.jeflette.mystory.data.remote.response.ListStoriesResponse
import com.jeflette.mystory.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MyStoryRepository
) : ViewModel() {

    private var _storiesResult: MutableStateFlow<Resource<ListStoriesResponse?>> =
        MutableStateFlow(Resource.Loading())
    val storiesResult: StateFlow<Resource<ListStoriesResponse?>> = _storiesResult

    private var _tokenResult: MutableStateFlow<String> = MutableStateFlow("")
    val tokenResult: StateFlow<String> = _tokenResult

    init {
        readToken()
    }

    fun getStories(value: String) {
        viewModelScope.launch {
            repository.getStories(token = "Bearer $value").collectLatest { it ->
                _storiesResult.value = it
            }
        }
    }

    private fun readToken() {
        viewModelScope.launch {
            repository.readToken().collect() { token ->
                _tokenResult.value = token
            }
        }
    }

    fun clearLogin(){
        viewModelScope.launch {
            repository.clearLogin()
        }
    }
}