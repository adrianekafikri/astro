package com.astro.test.adrian.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.test.adrian.Constant
import com.astro.test.adrian.util.FlowWrapper
import com.astro.test.adrian.domain.GithubUserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by adrianekafikri on 03/03/22.
 */
@HiltViewModel
class GithubUserViewModel @Inject constructor(
    private val interactor: GithubUserInteractor
) : ViewModel() {

    private val users = mutableListOf<GithubUserViewParam>()
    private val _usersStateFlow = FlowWrapper<GithubUserState>(GithubUserState.Initial)
    val usersFlow = _usersStateFlow.getFlow()
    private var since = 0
    var isAscending = true
    private set
    private var keyword = ""

    companion object {
        const val PAGE_LIMIT = 30
    }

    fun getAllUser() {
        if (users.size % PAGE_LIMIT == 0) {
            viewModelScope.launch {
                interactor.getAllUser(since, PAGE_LIMIT)
                    .onStart {
                        _usersStateFlow.postValue(GithubUserState.StartLoading)
                    }
                    .catch { e ->
                        _usersStateFlow.postValue(GithubUserState.Error(e.message.orEmpty()))
                    }
                    .collect {
                        since = it.lastOrNull()?.id ?: since
                        users.addAll(it)
                        filter(keyword)
                    }
            }
        }
    }

    fun filter(keyword: String) {
        this.keyword = keyword
        _usersStateFlow.postValue(GithubUserState.Initial)
        var tempList = mutableListOf<GithubUserViewParam>()
        tempList.addAll(users)
        if (keyword.isEmpty()) {
            tempList = tempList.sortedWith(compareBy { it.name }).toMutableList()
            if (!isAscending) {
                tempList = tempList.reversed().toMutableList()
            }
            _usersStateFlow.postValue(GithubUserState.Success(tempList))
        } else {
            tempList = tempList.filter { it.name.contains(keyword) }.sortedWith(compareBy { it.name }).toMutableList()
            if (!isAscending) {
                tempList = tempList.reversed().toMutableList()
            }
            _usersStateFlow.postValue(GithubUserState.Success(tempList))
        }
        if (tempList.isEmpty()) {
            _usersStateFlow.postValue(GithubUserState.Error(Constant.USER_NOT_FOUND))
        }
    }

    fun sort(isAscending: Boolean) {
        this.isAscending = isAscending
        filter(keyword)
    }
}