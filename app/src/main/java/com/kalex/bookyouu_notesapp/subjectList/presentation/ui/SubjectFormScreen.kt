package com.kalex.bookyouu_notesapp.subjectList.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material3.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.ui.composables.BYOutLineTextInputField
import com.kalex.bookyouu_notesapp.ui.composables.BottomSheetLayout

@OptIn( ExperimentalMaterialApi::class)
@Composable
fun SubjectForm() {
    var showSheet by remember { mutableStateOf(false) }
    var hideSheet by remember { mutableStateOf(false) }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
        )
    )
    BottomSheetLayout(
        scaffoldState = scaffoldState,
        scaffoldContent = {
            ScaffoldContent {
                showSheet = true
            }
        },
        sheetContent = {
            SheetContent {
                hideSheet = true
            }
        },
        showBottomSheet = { if (showSheet) it() },
        onBottomSheetHide = { hideSheet = false },
        hideBottomSheet = { if (hideSheet) it.invoke() },
        onBottomSheetShow = { showSheet = false },
    )
}

@Composable
fun SheetContent(
    onHideClick: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(128.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text("Swipe up to expand sheet")
    }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Sheet content")
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = { onHideClick() },
        ) {
            Text("Click to collapse sheet")
        }
    }
}

@Composable
fun ScaffoldContent(
    onShowSheet: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
    ) {
        Text(text = "Please fill all the information") // TODO: add styles and strings
        BYOutLineTextInputField(
            label = R.string.subject_form_subjectName_label,
            keyboardOptions = KeyboardOptions(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
            ),
            onTextChange = {},
        )
        BYOutLineTextInputField(
            label = R.string.subject_form_classRoom_label,
            keyboardOptions = KeyboardOptions(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
            ),
            onTextChange = {},
        )
        BYOutLineTextInputField(
            label = R.string.subject_form_credits_label,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
            onTextChange = {},
        )
        // Add check for 7 days
        Button(
            onClick = { onShowSheet() },
            content = {
            },
        )
    }
}
