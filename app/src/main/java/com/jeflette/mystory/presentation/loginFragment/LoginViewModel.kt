package com.jeflette.mystory.presentation.loginFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeflette.mystory.data.MyStoryRepository
import com.jeflette.mystory.data.remote.response.LoginResponse
import com.jeflette.mystory.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MyStoryRepository
) : ViewModel() {

    private var _loginResult: MutableStateFlow<Resource<LoginResponse?>> =
        MutableStateFlow(Resource.Loading())
    val loginResult: StateFlow<Resource<LoginResponse?>> = _loginResult

    private var _tokenResult: MutableStateFlow<String> = MutableStateFlow("")
    val tokenResult: StateFlow<String> get() = _tokenResult

    init {
        readToken()
    }

    fun submitLogin(email: String, password: String) {
        viewModelScope.launch {
            repository.submitLogin(email, password).collectLatest { loginResponse ->
                _loginResult.value = loginResponse
                saveToken(
                    loginResponse.data?.loginResult?.token ?: "",
                    loginResponse.data?.loginResult?.name ?: ""
                )
            }
        }
    }

    private fun saveToken(token: String, name: String) {
        viewModelScope.launch {
            repository.saveLogin(token, name)
        }
    }

    private fun readToken() {
        viewModelScope.launch {
            repository.readToken().collect() { token ->
                _tokenResult.value = token
            }
        }
    }
}