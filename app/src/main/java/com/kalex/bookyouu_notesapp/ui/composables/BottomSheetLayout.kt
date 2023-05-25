package com.kalex.bookyouu_notesapp.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        sheetShape = RoundedCornerShape(20.dp),
        sheetBackgroundColor = Color.LightGray,
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            scaffoldContent()
        }
    }
}
