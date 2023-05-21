package com.kalex.bookyouu_notesapp.subject.createsubject

import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.kalex.bookyouu_notesapp.subject.createsubject.presentation.SubjectFormInformationViewModel
import com.kalex.bookyouu_notesapp.ui.composables.BYBottomSheetLayout

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubjectForm(
    informationViewModel: SubjectFormInformationViewModel = hiltViewModel(),
) {
    var showSheet by remember { mutableStateOf(false) }
    var hideSheet by remember { mutableStateOf(false) }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
        ),
    )
    BYBottomSheetLayout(
        scaffoldState = scaffoldState,
        scaffoldContent = {
            ScaffoldContent(onShowSheet = { showSheet = true })
        },
        sheetContent = {
            SheetContent(
                onHideClick = {
                    hideSheet = true
                },
                onOptionSelected = {
                    informationViewModel.addDayOfWeek(it)
                },
                onOptionNotSelected = {
                    informationViewModel.deleteDayOfWeek(it)
                },
            )
        },
        showBottomSheet = { if (showSheet) it() },
        onBottomSheetHide = { hideSheet = false },
        hideBottomSheet = { if (hideSheet) it.invoke() },
        onBottomSheetShow = { showSheet = false },
    )
}
