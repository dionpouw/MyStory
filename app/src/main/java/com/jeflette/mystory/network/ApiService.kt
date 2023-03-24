package com.jeflette.mystory.network

import com.jeflette.mystory.data.remote.response.CommonResponse
import com.jeflette.mystory.data.remote.response.ListStoriesResponse
import com.jeflette.mystory.data.remote.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun submitRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun submitLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("stories")
    suspend fun getStories(@Header("Authorization") token: String): Response<ListStoriesResponse>

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<CommonResponse>
}