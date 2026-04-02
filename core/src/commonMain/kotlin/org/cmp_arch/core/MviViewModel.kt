package org.cmp_arch.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class MviViewModel<Intent : Any, State : Any, Effect : Any>(
    initialState: State,
) : ViewModel() {

    private val mutableState = MutableStateFlow(initialState)
    val state: StateFlow<State> = mutableState.asStateFlow()

    private val mutableEffect = MutableSharedFlow<Effect>(extraBufferCapacity = 1)
    val effect: SharedFlow<Effect> = mutableEffect.asSharedFlow()

    fun dispatch(intent: Intent) {
        onIntent(intent)
    }

    protected fun setState(reducer: (State) -> State) {
        mutableState.update(reducer)
    }

    protected fun postEffect(effect: Effect) {
        viewModelScope.launch {
            mutableEffect.emit(effect)
        }
    }

    protected abstract fun onIntent(intent: Intent)
}
