package com.kalex.bookyouu_notesapp.records

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.db.data.Note
import com.kalex.bookyouu_notesapp.records.data.NotesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagingRecordsViewModel @Inject constructor(
    private val notesRepositoryImpl: NotesRepository,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _notesList =
        MutableStateFlow<MutableList<Note>>(mutableListOf())
    val notesList: StateFlow<List<Note>>
        get() = _notesList.asStateFlow()

    private val _pagingState =
        MutableStateFlow<ListState>(ListState.LOADING)
    val pagingState: StateFlow<ListState>
        get() = _pagingState.asStateFlow()

    private var page = 0
    var canPaginate by mutableStateOf(false)

    fun getNotes(subjectID: Int) {
        if (page == 0 || (page != 0 && canPaginate) && _pagingState.value == ListState.IDLE) {
            _pagingState.update { if (page == 0) ListState.LOADING else ListState.PAGINATING }
        }
        viewModelScope.launch {
            try {
                val result = notesRepositoryImpl.getPagingNotesByDate(subjectID, 5, page * 5)

                canPaginate = result.size == 5

                if (page == 0) {
                    if (result.isEmpty()) {
                        _pagingState.update { ListState.EMPTY }
                        return@launch
                    }
                    _notesList.value.clear()
                    _notesList.value.addAll(result)
                } else {
                    _notesList.value.addAll(result)
                }

                _pagingState.update { ListState.IDLE }

                if (canPaginate) {
                    page++
                }

                if (!canPaginate) {
                    _pagingState.update { ListState.PAGINATION_EXHAUST }
                }
            } catch (e: Exception) {
                _pagingState.update { if (page == 0) ListState.ERROR else ListState.PAGINATION_EXHAUST }
            }
        }
    }

    fun clearPaging() {
        page = 0
        _pagingState.update { ListState.IDLE }
        canPaginate = false
    }
}

enum class ListState {
    IDLE,
    LOADING,
    PAGINATING,
    ERROR,
    PAGINATION_EXHAUST,
    EMPTY,
}
