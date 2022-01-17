package com.example.onehealth.domain.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

inline fun <T> Flow<T>.collectInScope(
    coroutineScope: CoroutineScope,
    dispatcher: CoroutineDispatcher,
    crossinline block: suspend (T) -> Unit
): Job {
    return coroutineScope.launch(dispatcher) {
        this@collectInScope.collect(block)
    }
}