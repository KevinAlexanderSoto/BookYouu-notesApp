package com.kalex.bookyouu_notesapp.subjectList.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import com.kalex.bookyouu_notesapp.ui.composables.BYBottomSheetLayout
import com.kalex.bookyouu_notesapp.ui.composables.BYRoundedCheckView
import com.kalex.bookyouu_notesapp.ui.composables.BYTextInput

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubjectForm() {
    var showSheet by remember { mutableStateOf(false) }
    var hideSheet by remember { mutableStateOf(false) }
    var selectedOptions by remember { mutableStateOf("") }
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(
            initialValue = BottomSheetValue.Collapsed,
        ),
    )
    BYBottomSheetLayout(
        scaffoldState = scaffoldState,
        scaffoldContent = {
            ScaffoldContent {
                showSheet = true
                selectedOptions
            }
        },
        sheetContent = {
            SheetContent(
                onHideClick = {
                    hideSheet = true
                },
                onOptionsSelected = {
                    selectedOptions = it
                },
            )
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
    onOptionsSelected: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Select the days of class", modifier = Modifier.weight(1f))
            IconButton(onClick = { onHideClick.invoke() }) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Play",
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(20.dp, 40.dp),
            content = {
                items(7) {
                    BYRoundedCheckView(text = "Miercoles", isRoundedChecked = {
                    })
                }
            },
        )
    }
}

@Composable
fun ScaffoldContent(
    onShowSheet: () -> String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
    ) {
        Text(text = "Please fill all the information") // TODO: add styles and strings
        BYTextInput.OutLinedTextField(
            label = R.string.subject_form_subjectName_label,
            keyboardOptions = KeyboardOptions(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
            ),
            onTextChange = {},
        )
        BYTextInput.OutLinedTextField(
            label = R.string.subject_form_classRoom_label,
            keyboardOptions = KeyboardOptions(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
            ),
            onTextChange = {},
        )
        BYTextInput.OutLinedTextField(
            label = R.string.subject_form_credits_label,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
            onTextChange = {},
        )
        BYTextInput.OutLinedButtonTextField(label = R.string.subject_form_credits_label) {
            onShowSheet()
        }

        Button(onClick = {
            //TODO: Create subject
        }) {
            Text(text = "Create Subject")
        }
        // Add check for 7 days
    }
}
