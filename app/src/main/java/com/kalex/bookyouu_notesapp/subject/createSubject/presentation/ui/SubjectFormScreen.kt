package com.kalex.bookyouu_notesapp.subject.createSubject.presentation.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.bookyouu_notesapp.core.common.composables.BYBottomSheetLayout
import com.kalex.bookyouu_notesapp.core.common.composables.BYLoadingIndicator
import com.kalex.bookyouu_notesapp.core.common.handleViewModelState
import com.kalex.bookyouu_notesapp.permission.RequireNotificationPermission
import com.kalex.bookyouu_notesapp.subject.createSubject.presentation.SubjectFormInformationViewModel
import com.kalex.bookyouu_notesapp.subject.createSubject.presentation.SubjectFormViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun SubjectForm(
    paddingValues: PaddingValues,
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
    val context = LocalContext.current
    RequireNotificationPermission(
        onPermissionDenied = {
            context.startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                },
            )
        },
        content = {
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
        },
    )
}
