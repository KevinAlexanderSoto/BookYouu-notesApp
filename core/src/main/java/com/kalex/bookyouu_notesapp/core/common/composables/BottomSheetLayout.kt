package com.kalex.bookyouu_notesapp.core.common.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BYBottomSheetLayout(
    scaffoldState: BottomSheetScaffoldState,
    scaffoldContent: @Composable () -> Unit,
    sheetContent: @Composable () -> Unit,
) {
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetElevation = 40.dp,
        sheetContent = { sheetContent.invoke() },
        sheetShape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.secondaryContainer,
        backgroundColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            scaffoldContent()
        }
    }
}
