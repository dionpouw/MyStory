package com.jeflette.mystory.presentation.addStoryFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeflette.mystory.data.MyStoryRepository
import com.jeflette.mystory.data.remote.response.CommonResponse
import com.jeflette.mystory.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val repository: MyStoryRepository
) : ViewModel() {

    private var _addStoryResult: MutableStateFlow<Resource<CommonResponse?>> =
        MutableStateFlow(Resource.Loading())
    val addStoryResult: StateFlow<Resource<CommonResponse?>> = _addStoryResult

    private var _tokenResult: MutableStateFlow<String> = MutableStateFlow("")
    val tokenResult: StateFlow<String> get() = _tokenResult

    init {
        readToken()
    }

    private fun readToken() {
        viewModelScope.launch {
            repository.readToken().collect() { token ->
                _tokenResult.value = token
            }
        }
    }

    fun addStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            repository.addStory(token = "Bearer $token", file, description)
                .collectLatest { addStoryResponse ->
                    _addStoryResult.value = addStoryResponse
                }
        }
    }
}