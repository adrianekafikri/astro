package com.astro.test.adrian.domain

import com.astro.test.adrian.presentation.GithubUserViewParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by adrianekafikri on 03/03/22.
 */
class GithubUserInteractorImpl @Inject constructor(
    private val repository: GithubUserRepository
) : GithubUserInteractor {

    override fun getAllUser(since: Int, limit: Int): Flow<List<GithubUserViewParam>> =
        repository.getAllUser(since, limit).map {
            it.map { item -> item.toViewParam() }
        }
}