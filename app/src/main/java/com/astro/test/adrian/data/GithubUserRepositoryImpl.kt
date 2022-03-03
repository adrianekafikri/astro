package com.astro.test.adrian.data

import com.astro.test.adrian.data.GithubUserApiService
import com.astro.test.adrian.data.GithubUserEntity
import com.astro.test.adrian.domain.GithubUserRepository
import com.astro.test.adrian.util.catchError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

/**
 * Created by adrianekafikri on 03/03/22.
 */
class GithubUserRepositoryImpl constructor(
    private val apiService: GithubUserApiService
) : GithubUserRepository {

    override fun getAllUser(since: Int, limit: Int): Flow<List<GithubUserEntity>> {
        return flow {
            emit(apiService.getAllUser(since = since, perPage = limit))
        }.catch {
            it.catchError()
        }
    }
}