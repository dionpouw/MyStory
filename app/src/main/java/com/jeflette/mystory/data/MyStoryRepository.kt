package com.jeflette.mystory.data

import com.jeflette.mystory.data.local.preference.LoginPreference
import com.jeflette.mystory.data.remote.RemoteDataSource
import com.jeflette.mystory.data.remote.response.CommonResponse
import com.jeflette.mystory.data.remote.response.ListStoriesResponse
import com.jeflette.mystory.data.remote.response.LoginResponse
import com.jeflette.mystory.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class MyStoryRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource, private val loginPreference: LoginPreference
) {
    suspend fun getStories(token: String): Flow<Resource<ListStoriesResponse?>> {
        return wrapWithFlow(
            remoteDataSource::getStories
                .invoke(token)
        )
    }

    suspend fun addStory(
        token: String, file: MultipartBody.Part, description: RequestBody
    ): Flow<Resource<CommonResponse?>> = flow {
        wrapWithFlow(remoteDataSource::addStory.invoke(token, file, description))
    }

    suspend fun submitRegister(
        name: String, email: String, password: String
    ): Flow<Resource<CommonResponse?>> {
        return wrapWithFlow(
            remoteDataSource::submitRegister
                .invoke(name, email, password)
        )
    }

    suspend fun submitLogin(email: String, password: String): Flow<Resource<LoginResponse?>> {
        return wrapWithFlow(
            remoteDataSource::submitLogin
                .invoke(email, password)
        )
    }

    suspend fun saveLogin(token: String, username: String) {
        loginPreference.saveLogin(token, username)
    }

    suspend fun clearLogin() {
        loginPreference.clearLogin()
    }

    fun readToken(): Flow<String> {
        return loginPreference.readToken()
    }

    suspend fun readUsername(): Flow<String> {
        return loginPreference.readUsername()
    }

    private fun <T> wrapWithFlow(function: Response<T>): Flow<Resource<T?>> {
        return flow {
            emit(Resource.Loading())
            try {
                if (function.isSuccessful) {
                    emit(Resource.Success(function.body()))
                } else {
                    emit(Resource.Error(throwable = Throwable(function.message())))
                }
            } catch (e: Exception) {
                emit(Resource.Error(throwable = e))
            }
        }
    }
}