package com.jeflette.mystory.di

import com.jeflette.mystory.BuildConfig
import com.jeflette.mystory.BuildConfig.BASE_URL
import com.jeflette.mystory.data.MyStoryRepository
import com.jeflette.mystory.data.local.preference.LoginPreference
import com.jeflette.mystory.data.remote.RemoteDataSource
import com.jeflette.mystory.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = if (BuildConfig.DEBUG) {
        OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        ).client(okHttpClient).build()

    @Provides
    @Singleton
    fun provideStoriesApi(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService) = RemoteDataSource(apiService)

    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: RemoteDataSource,
        loginPreference: LoginPreference
    ) = MyStoryRepository(remoteDataSource,loginPreference)
}