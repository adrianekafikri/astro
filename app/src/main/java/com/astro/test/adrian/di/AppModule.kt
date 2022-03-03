package com.astro.test.adrian.di

import android.content.Context
import com.astro.test.adrian.BuildConfig
import com.astro.test.adrian.data.GithubUserApiService
import com.astro.test.adrian.data.GithubUserRepositoryImpl
import com.astro.test.adrian.domain.GithubUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Created by adrianekafikri on 03/03/22.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    companion object {
        private const val HEADER_ACCEPT_KEY = "Accept"
        private const val HEADER_ACCEPT_VALUE = "application/vnd.github.v3+json"
        private const val HEADER_ACCESS_TOKEN_KEY = "access_token"
    }

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_API_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header(HEADER_ACCEPT_KEY, HEADER_ACCEPT_VALUE)
                .header(HEADER_ACCESS_TOKEN_KEY, BuildConfig.ACCESS_TOKEN)
            chain.proceed(requestBuilder.build())
        }
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideGithubApiService(retrofit: Retrofit): GithubUserApiService =
        retrofit.create(GithubUserApiService::class.java)

    @Provides
    @Singleton
    fun provideGithubRepository(apiService: GithubUserApiService): GithubUserRepository =
        GithubUserRepositoryImpl(apiService)
}