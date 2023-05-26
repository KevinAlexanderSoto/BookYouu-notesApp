package com.kalex.bookyouu_notesapp.subject.createSubject.presentation.ui

import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.kalex.bookyouu_notesapp.subject.createSubject.ViewModelState
import com.kalex.bookyouu_notesapp.subject.createSubject.presentation.SubjectFormInformationViewModel
import com.kalex.bookyouu_notesapp.subject.createSubject.presentation.SubjectFormViewModel
import com.kalex.bookyouu_notesapp.ui.composables.BYBottomSheetLayout
import com.kalex.bookyouu_notesapp.ui.composables.BYLoadingIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubjectForm(
    onNavigateToConfirmationScreen: () -> Unit,
    informationViewModel: SubjectFormInformationViewModel = hiltViewModel(),
    formViewModel: SubjectFormViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    var showLoadingProgressBar by remember { mutableStateOf(false) }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
        ),
    )
    BYBottomSheetLayout(
        scaffoldState = scaffoldState,
        scaffoldContent = {
            if (showLoadingProgressBar) {
                BYLoadingIndicator()
            }
            ScaffoldContent(
                onShowSheet = { scope.launch { scaffoldState.bottomSheetState.expand() } },
                onCreateSubjectClick = {
                    formViewModel.createSubject(it)
                    handleCreationState(
                        formViewModel.createSubjectState,
                        scope,
                        onLoading = { showLoadingProgressBar = true },
                        onError = {
                            // TODO:AHOW GENERERIC ERROR
                        },
                        onSuccess = { onNavigateToConfirmationScreen() },
                    )
                },
            )
        },
        sheetContent = {
            SheetContent(
                onHideClick = { scope.launch { scaffoldState.bottomSheetState.collapse() } },
                onOptionSelected = {
                    informationViewModel.addDayOfWeek(it)
                },
                onOptionNotSelected = {
                    informationViewModel.deleteDayOfWeek(it)
                },
            )
        },
    )
}

fun handleCreationState(
    collectAsState: StateFlow<ViewModelState<Boolean>>,
    scope: CoroutineScope,
    onLoading: () -> Unit,
    onSuccess: () -> Unit,
    onError: () -> Unit,
) {
    scope.launch {
        collectAsState.collectLatest {
            when (it) {
                is ViewModelState.Error -> onError()
                is ViewModelState.Loading -> onLoading()
                is ViewModelState.Success -> onSuccess()
                else -> {
                    onError()
                }
            }
        }
    }
}
