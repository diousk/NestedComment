package com.lang.commentsystem.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import kotlin.reflect.KProperty1

// A copy version of tivi/common-ui-view/src/main/java/app/tivi/ReduxViewModel.kt
// from https://github.com/chrisbanes/tivi
abstract class ReduxViewModel<S> constructor(initialState: S) : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    private val stateMutex = Mutex()

    val defaultErrorHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.w(throwable)
    }

    val state: StateFlow<S>
        get() = _state.asStateFlow()

    protected suspend fun <T> Flow<T>.collectAndSetState(reducer: S.(T) -> S) {
        return collect { item -> setState { reducer(item) } }
    }

    fun <A> selectObserve(prop1: KProperty1<S, A>): Flow<A> {
        return selectSubscribe(prop1)
    }

    protected fun subscribe(block: (S) -> Unit) {
        viewModelScope.launch(defaultErrorHandler) {
            _state.collect { block(it) }
        }
    }

    protected fun <A> selectSubscribe(prop1: KProperty1<S, A>, block: (A) -> Unit) {
        viewModelScope.launch(defaultErrorHandler) {
            selectSubscribe(prop1).collect { block(it) }
        }
    }

    private fun <A> selectSubscribe(prop1: KProperty1<S, A>): Flow<A> {
        return _state.map { prop1.get(it) }.distinctUntilChanged()
    }

    protected suspend fun setState(reducer: S.() -> S) {
        stateMutex.withLock {
            _state.value = reducer(_state.value)
        }
    }

    protected fun CoroutineScope.launchSetState(reducer: S.() -> S) {
        launch { this@ReduxViewModel.setState(reducer) }
    }

    protected suspend fun withState(block: (S) -> Unit) {
        stateMutex.withLock {
            block(_state.value)
        }
    }

    protected fun CoroutineScope.withState(block: (S) -> Unit) {
        launch(defaultErrorHandler) { this@ReduxViewModel.withState(block) }
    }
}