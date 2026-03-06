package com.kalex.bookyouu_notesapp.subject.createSubject.presentation.ui

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.Density
import com.kalex.bookyouu_notesapp.core.common.composables.BYBottomSheetLayout
import com.kalex.bookyouu_notesapp.core.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.core.common.handleViewModelState
import com.kalex.bookyouu_notesapp.subject.createSubject.presentation.SubjectFormInformationViewModel
import com.kalex.bookyouu_notesapp.subject.createSubject.presentation.SubjectFormViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubjectForm(
    paddingValues: PaddingValues,
    onNavigateToConfirmationScreen: () -> Unit,
    informationViewModel: SubjectFormInformationViewModel = koinViewModel(),
    formViewModel: SubjectFormViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    var showLoadingProgressBar by remember { mutableStateOf(false) }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
            density = Density(density = 1f),
        ),
    )
    BYBottomSheetLayout(
        scaffoldState = scaffoldState,
        scaffoldContent = {
            if (showLoadingProgressBar) {
                BYLoadingIndicator()
            }
            ScaffoldContent(
                paddingValues = paddingValues,
                onShowSheet = { scope.launch { scaffoldState.bottomSheetState.expand() } },
                onCreateSubjectClick = {
                    formViewModel.createSubject(it)
                    handleViewModelState(
                        formViewModel.createSubjectState,
                        scope,
                        onLoading = { showLoadingProgressBar = true },
                        onError = {
                            // TODO:SHOW GENERERIC ERROR
                        },
                        onSuccess = { onNavigateToConfirmationScreen() },
                        onEmpty = {
                        },
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
