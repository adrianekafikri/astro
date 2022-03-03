package com.astro.test.adrian.domain

import com.astro.test.adrian.data.GithubUserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by adrianekafikri on 03/03/22.
 */
interface GithubUserRepository {

    fun getAllUser(since: Int, limit: Int): Flow<List<GithubUserEntity>>
}