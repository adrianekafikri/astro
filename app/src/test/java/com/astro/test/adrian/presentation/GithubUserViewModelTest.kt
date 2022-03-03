package com.astro.test.adrian.presentation

import com.astro.test.adrian.Constant
import com.astro.test.adrian.domain.GithubUserInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
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
class GithubUserViewModelTest {

    @Mock
    private lateinit var interactor: GithubUserInteractor

    private lateinit var viewModel: GithubUserViewModel

    private val mainThreadSurrogate = TestCoroutineDispatcher()

    private val testCoroutineScope = TestCoroutineScope(mainThreadSurrogate)

    private val since = 0
    private val pageLimit = 3

    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.initMocks(this)
        viewModel = GithubUserViewModel(interactor)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testCoroutineScope.cleanupTestCoroutines()
    }

    private fun getResult(): List<GithubUserViewParam> {
        return listOf(
            GithubUserViewParam(
                id = 1,
                avatar = "https://avatars.githubusercontent.com/u/1?v=4",
                name = "mojombo"
            ),
            GithubUserViewParam(
                id = 2,
                avatar = "https://avatars.githubusercontent.com/u/2?v=4",
                name = "defunkt"
            ),
            GithubUserViewParam(
                id = 3,
                avatar = "https://avatars.githubusercontent.com/u/3?v=4",
                name = "pjhyett"
            )
        )
    }

    @Test
    fun `Test getAllUser failed`() = testCoroutineScope.runBlockingTest {
        val expected = "Network Error"
        var actual: String? = null

        Mockito.`when`(interactor.getAllUser(since, pageLimit)).thenReturn(
            flow { throw Throwable(expected) }
        )

        viewModel.getAllUser()

        launch {
            viewModel.usersFlow.collect {
                if (it is GithubUserState.Error) {
                    actual = it.message
                }

                assertEquals(expected, actual)
            }
        }.cancel()
    }

    @Test
    fun `Test getAllUser success with sort ascending`() = testCoroutineScope.runBlockingTest {
        val result = getResult()

        val expected = listOf(
            GithubUserViewParam(
                id = 2,
                avatar = "https://avatars.githubusercontent.com/u/2?v=4",
                name = "defunkt"
            ),
            GithubUserViewParam(
                id = 1,
                avatar = "https://avatars.githubusercontent.com/u/1?v=4",
                name = "mojombo"
            ),
            GithubUserViewParam(
                id = 3,
                avatar = "https://avatars.githubusercontent.com/u/3?v=4",
                name = "pjhyett"
            )
        )
        var actual: List<GithubUserViewParam>? = null

        Mockito.`when`(interactor.getAllUser(since, pageLimit)).thenReturn(
            flow {
                emit(result)
            }
        )

        viewModel.getAllUser()

        launch {
            viewModel.usersFlow.collect {
                if (it is GithubUserState.Success) {
                    actual = it.list
                }

                assertEquals(expected, actual)
                assertEquals(expected.size, actual?.size)
            }
        }.cancel()
    }

    @Test
    fun `Test getAllUser success with sort descending`() = testCoroutineScope.runBlockingTest {
        val result = getResult()

        val expected = listOf(
            GithubUserViewParam(
                id = 3,
                avatar = "https://avatars.githubusercontent.com/u/3?v=4",
                name = "pjhyett"
            ),
            GithubUserViewParam(
                id = 1,
                avatar = "https://avatars.githubusercontent.com/u/1?v=4",
                name = "mojombo"
            ),
            GithubUserViewParam(
                id = 2,
                avatar = "https://avatars.githubusercontent.com/u/2?v=4",
                name = "defunkt"
            )
        )
        var actual: List<GithubUserViewParam>? = null

        Mockito.`when`(interactor.getAllUser(since, pageLimit)).thenReturn(
            flow {
                emit(result)
            }
        )

        viewModel.getAllUser()
        viewModel.sort(false)

        launch {
            viewModel.usersFlow.collect {
                if (it is GithubUserState.Success) {
                    actual = it.list
                }

                assertEquals(expected, actual)
                assertEquals(expected.size, actual?.size)
                assertEquals(false, viewModel.isAscending)
            }
        }.cancel()
    }

    @Test
    fun `Test getAllUser success with sort ascending with keyword`() = testCoroutineScope.runBlockingTest {
        val result = getResult()

        val expected = listOf(
            GithubUserViewParam(
                id = 2,
                avatar = "https://avatars.githubusercontent.com/u/2?v=4",
                name = "defunkt"
            ),
            GithubUserViewParam(
                id = 3,
                avatar = "https://avatars.githubusercontent.com/u/3?v=4",
                name = "pjhyett"
            )
        )
        var actual: List<GithubUserViewParam>? = null

        Mockito.`when`(interactor.getAllUser(since, pageLimit)).thenReturn(
            flow {
                emit(result)
            }
        )

        viewModel.getAllUser()
        viewModel.sort(true)
        viewModel.filter("t")

        launch {
            viewModel.usersFlow.collect {
                if (it is GithubUserState.Success) {
                    actual = it.list
                }

                assertEquals(expected, actual)
                assertEquals(expected.size, actual?.size)
                assertEquals(true, viewModel.isAscending)
            }
        }.cancel()
    }

    @Test
    fun `Test getAllUser success with sort descending with keyword`() = testCoroutineScope.runBlockingTest {
        val result = getResult()

        val expected = listOf(
            GithubUserViewParam(
                id = 1,
                avatar = "https://avatars.githubusercontent.com/u/1?v=4",
                name = "mojombo"
            )
        )
        var actual: List<GithubUserViewParam>? = null

        Mockito.`when`(interactor.getAllUser(since, pageLimit)).thenReturn(
            flow {
                emit(result)
            }
        )

        viewModel.getAllUser()
        viewModel.sort(false)
        viewModel.filter("m")

        launch {
            viewModel.usersFlow.collect {
                if (it is GithubUserState.Success) {
                    actual = it.list
                }

                assertEquals(expected, actual)
                assertEquals(expected.size, actual?.size)
                assertEquals(false, viewModel.isAscending)
            }
        }.cancel()
    }

    @Test
    fun `Test getAllUser success but filter with no user found`() = testCoroutineScope.runBlockingTest {
        val result = listOf<GithubUserViewParam>()
        val expected = Constant.USER_NOT_FOUND
        var actual: String? = null

        Mockito.`when`(interactor.getAllUser(since, pageLimit)).thenReturn(
            flow {
                emit(result)
            }
        )

        viewModel.getAllUser()
        viewModel.filter("m")

        launch {
            viewModel.usersFlow.collect {
                if (it is GithubUserState.Error) {
                    actual = it.message
                }

                assertEquals(expected, actual)
            }
        }.cancel()
    }
}