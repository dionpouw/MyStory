package com.jeflette.mystory.presentation.registerFragment

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
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: MyStoryRepository
) : ViewModel() {

    private var _registerResult: MutableStateFlow<Resource<CommonResponse?>> =
        MutableStateFlow(Resource.Loading())
    val registerResult: StateFlow<Resource<CommonResponse?>> = _registerResult

    fun submitRegister(name: String, email: String, password: String) {
        viewModelScope.launch {
            repository.submitRegister(name, email, password).collectLatest { registerResponse ->
                _registerResult.value = registerResponse
            }
        }
    }
}