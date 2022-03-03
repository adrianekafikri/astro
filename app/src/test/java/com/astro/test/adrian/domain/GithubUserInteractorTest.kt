package com.astro.test.adrian.domain

import com.astro.test.adrian.data.GithubUserEntity
import com.astro.test.adrian.presentation.GithubUserViewParam
import com.squareup.moshi.Moshi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

/**
 * Created by adrianekafikri on 03/03/22.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class GithubUserInteractorTest {

    @Mock
    private lateinit var repository: GithubUserRepository

    private lateinit var interactor: GithubUserInteractor

    private val since = 0
    private val pageLimit = 3

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        interactor = GithubUserInteractorImpl(repository)
    }

    @Test
    fun `Test getAllUser`() {
        val entity = listOf(
            GithubUserEntity(
                login = "mojombo",
                id = 1,
                avatar_url = "https://avatars.githubusercontent.com/u/1?v=4",
                node_id = "",
                gravatar_id = "",
                url = "",
                html_url = "",
                followers_url = "",
                following_url = "",
                gists_url = "",
                starred_url = "",
                subscriptions_url = "",
                organizations_url = "",
                repos_url = "",
                events_url = "",
                received_events_url = "",
                type = "",
                site_admin = false
            ),
            GithubUserEntity(
                login = "defunkt",
                id = 2,
                avatar_url = "https://avatars.githubusercontent.com/u/2?v=4",
                node_id = "",
                gravatar_id = "",
                url = "",
                html_url = "",
                followers_url = "",
                following_url = "",
                gists_url = "",
                starred_url = "",
                subscriptions_url = "",
                organizations_url = "",
                repos_url = "",
                events_url = "",
                received_events_url = "",
                type = "",
                site_admin = false
            ),
            GithubUserEntity(
                login = "pjhyett",
                id = 3,
                avatar_url = "https://avatars.githubusercontent.com/u/3?v=4",
                node_id = "",
                gravatar_id = "",
                url = "",
                html_url = "",
                followers_url = "",
                following_url = "",
                gists_url = "",
                starred_url = "",
                subscriptions_url = "",
                organizations_url = "",
                repos_url = "",
                events_url = "",
                received_events_url = "",
                type = "",
                site_admin = false
            )
        )
        val expected = entity.map { it.toViewParam() }
        var actual: List<GithubUserViewParam> = listOf()

        Mockito.`when`(repository.getAllUser(since, pageLimit)).thenReturn(
            flow { emit(entity) }
        )

        runBlockingTest {
            interactor.getAllUser(since, pageLimit).collect {
                actual = it
            }

            assertEquals(expected, actual)
        }
    }
}