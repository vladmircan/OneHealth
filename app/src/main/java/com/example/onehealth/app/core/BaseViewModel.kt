package com.example.onehealth.app.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onehealth.app.utils.userDisplayMessageId
import com.example.onehealth.data.error_handling.fromThrowable
import com.example.onehealth.domain.core.AppDispatchers
import com.example.onehealth.domain.core.Failure
import com.example.onehealth.domain.core.UseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

abstract class BaseViewModel: ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading

    private var numberOfRunningTasks = 0
    val isIdle
        get() = numberOfRunningTasks == 0

    protected val _failure = MutableSharedFlow<Failure>()
    val failure: Flow<Failure> = _failure

    @Inject
    lateinit var exceptionTracker: com.example.onehealth.domain.error_handling.ExceptionTracker

    protected fun incrementNumberOfRunningTasks() {
        if (numberOfRunningTasks == 0)
            _isLoading.value = true
        ++numberOfRunningTasks
    }

    protected fun decrementNumberOfRunningTasks() {
        when (numberOfRunningTasks) {
            0 -> return
            1 -> _isLoading.value = false
        }
        --numberOfRunningTasks
    }

    protected open fun handleFailure(failure: Failure) {
        Timber.e(failure.exception)
        viewModelScope.launch(AppDispatchers.IO) {
            _failure.emit(failure)
            _errorMessageId.emit(failure.userDisplayMessageId)
            exceptionTracker.trackFailure(failure)
        }
    }

    protected fun <T: UseCase.Params, K: Any?> UseCase<T, K>.perform(
        input: T,
        onSuccess: (K) -> Unit = {}
    ) = perform(input, onSuccess, {})

    protected fun <T: UseCase.Params, K: Any?> UseCase<T, K>.perform(
        input: T,
        onSuccess: (K) -> Unit,
        onFailure: (Failure) -> Unit
    ): Job = viewModelScope.launch(AppDispatchers.IO) {
        incrementNumberOfRunningTasks()
        val result = this@perform(input)
        when {
            result.isSuccess -> onSuccess(result.getOrThrow())
            result.isFailure -> {
                val failure = Failure.fromThrowable(result.exceptionOrNull()!!)
                onFailure(failure)
                this@BaseViewModel.handleFailure(failure)
            }
        }
        decrementNumberOfRunningTasks()
    }

    companion object {
        private val _errorMessageId = MutableSharedFlow<Int?>()
        val errorMessageId: Flow<Int> = _errorMessageId.filterNotNull()
    }
}