package com.kalex.bookyouu_notesapp.subject.createSubject

sealed class ViewModelState<T> {
    data class Success<T>(val data: T) : ViewModelState<T>()
    data class Loading<T>(val isLoading: Boolean) : ViewModelState<T>()
    data class Error<T>(val exception: Exception) : ViewModelState<T>()
}
