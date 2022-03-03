package com.astro.test.adrian.presentation

/**
 * Created by adrianekafikri on 03/03/22.
 */
sealed class GithubUserState {

    class Success(val list: List<GithubUserViewParam>) : GithubUserState()
    class Error(val message: String) : GithubUserState()
    object StartLoading : GithubUserState()
    object Initial : GithubUserState()
}
