package com.astro.test.adrian.data

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by adrianekafikri on 03/03/22.
 */
interface GithubUserApiService {

    @GET("users")
    suspend fun getAllUser(
        @Query ("per_page") perPage: Int,
        @Query ("since") since: Int
    ): List<GithubUserEntity>
}