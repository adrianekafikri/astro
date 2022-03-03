package com.astro.test.adrian.domain

import com.astro.test.adrian.presentation.GithubUserViewParam
import kotlinx.coroutines.flow.Flow

/**
 * Created by adrianekafikri on 03/03/22.
 */
interface GithubUserInteractor {

    fun getAllUser(since: Int, limit: Int): Flow<List<GithubUserViewParam>>
}