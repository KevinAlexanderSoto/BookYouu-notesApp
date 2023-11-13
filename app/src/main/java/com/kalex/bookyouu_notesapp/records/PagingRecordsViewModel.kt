package com.kalex.bookyouu_notesapp.records

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalex.bookyouu_notesapp.db.data.Note
import com.kalex.bookyouu_notesapp.records.data.NotesRepository
import com.kalex.bookyouu_notesapp.records.recordList.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PagingRecordsViewModel @Inject constructor(
    private val notesRepositoryImpl: NotesRepository,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _notesList =
        MutableStateFlow<MutableList<Category>>(mutableListOf())
    val notesList: StateFlow<List<Category>>
        get() = _notesList.asStateFlow()

    private val _pagingState =
        MutableStateFlow<PaginationState>(PaginationState.LOADING)
    val pagingState: StateFlow<PaginationState>
        get() = _pagingState.asStateFlow()

    private var page = INITIAL_PAGE
    var canPaginate by mutableStateOf(false)

    fun getNotes(subjectID: Int) {
        if (page == INITIAL_PAGE || (canPaginate) && _pagingState.value == PaginationState.REQUEST_INACTIVE) {
            _pagingState.update { if (page == INITIAL_PAGE) PaginationState.LOADING else PaginationState.PAGINATING }
        }
        viewModelScope.launch(dispatcher) {
            try {
                val result =
                    notesRepositoryImpl.getPagingNotesByDate(subjectID, PAGE_SIZE, page * PAGE_SIZE)
                canPaginate = result.size == PAGE_SIZE

                if (page == INITIAL_PAGE) {
                    if (result.isEmpty()) {
                        _pagingState.update { PaginationState.EMPTY }
                        return@launch
                    }
                    _notesList.value.clear()

                    val dataMap = sortedListNotesToCategory(result)
                    _notesList.value.addAll(dataMap)

                } else {
                    val dataMap = sortedListNotesToCategory(result)
                    _notesList.value.addAll(dataMap)
                }

                _pagingState.update { PaginationState.REQUEST_INACTIVE }

                if (canPaginate) {
                    page++
                }

                if (!canPaginate) {
                    _pagingState.update { PaginationState.PAGINATION_EXHAUST }
                }
            } catch (e: Exception) {
                _pagingState.update { if (page == INITIAL_PAGE) PaginationState.ERROR else PaginationState.PAGINATION_EXHAUST }
            }
        }
    }

    private fun sortedListNotesToCategory(result: List<Note>): List<Category> {
        val format = SimpleDateFormat("dd/MM/yyyy")

        return result.groupBy {
            format.format(it.noteDate)
        }
            .toSortedMap(reverseOrder())
            .map {
                Category(
                    name = it.key.toString(),
                    items = it.value
                )
            }
    }

    fun clearPaging() {
        page = INITIAL_PAGE
        _pagingState.update { PaginationState.LOADING }
        canPaginate = false
    }

    companion object {
        const val PAGE_SIZE = 5
        const val INITIAL_PAGE = 0
    }
}

enum class PaginationState {
    REQUEST_INACTIVE,
    LOADING,
    PAGINATING,
    ERROR,
    PAGINATION_EXHAUST,
    EMPTY,
}
