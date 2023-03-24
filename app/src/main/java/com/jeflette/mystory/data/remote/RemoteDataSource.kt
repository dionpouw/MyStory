package com.jeflette.mystory.data.remote

import com.jeflette.mystory.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getStories(token: String) = apiService.getStories(token)

    suspend fun addStory(token: String, file: MultipartBody.Part, description: RequestBody) =
        apiService.addStory(token, file, description)

    suspend fun submitRegister(name: String, email: String, password: String) =
        apiService.submitRegister(name, email, password)

    suspend fun submitLogin(email: String, password: String) =
        apiService.submitLogin(email, password)


}