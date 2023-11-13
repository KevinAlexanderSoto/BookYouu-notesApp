package com.kalex.bookyouu_notesapp.core.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * This function is to handle the viewModel state when we are not into a compose scope
 * **/
fun <T> handleViewModelState(
    collectAsState: StateFlow<ViewModelState<T>>,
    scope: CoroutineScope,
    onLoading: () -> Unit,
    onSuccess: (T) -> Unit,
    onEmpty: () -> Unit,
    onError: () -> Unit,
) {
    scope.launch {
        collectAsState.collectLatest {
            when (it) {
                is ViewModelState.Error -> onError()
                is ViewModelState.Loading -> onLoading()
                is ViewModelState.Success -> {
                    onSuccess(it.data)
                }

                is ViewModelState.Empty -> onEmpty()
                else -> {
                    onError()
                }
            }
        }
    }
}

/**
 * This function is to handle the viewModel state when we are into a compose scope
 * **/
@Composable
fun <T> handleViewModelState(
    stateFlow: State<ViewModelState<T>>,
    onLoading: @Composable () -> Unit,
    onSuccess: @Composable (T) -> Unit,
    onEmpty: @Composable () -> Unit,
    onError: @Composable (exception: Exception) -> Unit,
) {
    when (val response = stateFlow.value) {
        is ViewModelState.Empty -> {
            onEmpty()
        }

        is ViewModelState.Error -> onError(response.exception)
        is ViewModelState.Loading -> onLoading()
        is ViewModelState.Success -> {
            onSuccess(response.data)
        }

        else -> {}
    }
}
