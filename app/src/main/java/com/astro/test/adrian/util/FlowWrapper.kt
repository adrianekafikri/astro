package com.astro.test.adrian.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Created by adrianekafikri on 03/03/22.
 */
class FlowWrapper<T : Any>(initialValue: T?) {
    private val stateFlow = MutableStateFlow(initialValue)

    fun postValue(value: T) {
        stateFlow.value = value
    }

    fun getValue(): T? = stateFlow.value

    fun getFlow() = stateFlow as StateFlow<T?>
}