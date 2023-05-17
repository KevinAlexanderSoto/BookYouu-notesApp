package com.kalex.bookyouu_notesapp.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetLayout(
    scaffoldState: BottomSheetScaffoldState,
    scaffoldContent: @Composable () -> Unit,
    sheetContent: @Composable () -> Unit,
    showBottomSheet: (() -> Unit) -> Unit,
    hideBottomSheet: (() -> Unit) -> Unit,
    onBottomSheetHide: () -> Unit,
    onBottomSheetShow: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    hideBottomSheet {
        scope.launch { scaffoldState.bottomSheetState.collapse() }.invokeOnCompletion {
            onBottomSheetHide()
        }
    }

    showBottomSheet {
        scope.launch { scaffoldState.bottomSheetState.expand() }.invokeOnCompletion {
            onBottomSheetShow()
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = { sheetContent.invoke() },
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            scaffoldContent()
        }
    }
}
