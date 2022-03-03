package com.astro.test.adrian.domain

import com.astro.test.adrian.data.GithubUserEntity
import com.astro.test.adrian.presentation.GithubUserViewParam

/**
 * Created by adrianekafikri on 03/03/22.
 */
fun GithubUserEntity.toViewParam(): GithubUserViewParam {
    return GithubUserViewParam(
        id = this.id ?: 0,
        avatar = this.avatar_url.orEmpty(),
        name = this.login.orEmpty()
    )
}