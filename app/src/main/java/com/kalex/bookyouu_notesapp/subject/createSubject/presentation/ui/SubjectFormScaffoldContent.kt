package com.kalex.bookyouu_notesapp.subject.createSubject.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.core.common.composables.BYTextInput
import com.kalex.bookyouu_notesapp.db.data.Subject
import com.kalex.bookyouu_notesapp.subject.createSubject.presentation.SubjectFormInformationViewModel

@Composable
fun ScaffoldContent(
    paddingValues: PaddingValues,
    onShowSheet: () -> Unit,
    onCreateSubjectClick: (Subject) -> Unit,
    informationViewModel: SubjectFormInformationViewModel = hiltViewModel(),
) {
    val dayList = informationViewModel.getListOfStringSelectedDays().map {
        stringResource(id = it)
    }.toList().joinToString()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
    ) {
        val localFocusManager: FocusManager = LocalFocusManager.current

        Text(text = stringResource(id = R.string.subject_form_title))
        BYTextInput.OutLinedTextField(
            label = R.string.subject_form_subjectName_label,
            keyboardOptions = KeyboardOptions(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
            ),
            onTextChange = {
                informationViewModel.setSubjectName(it)
            },
            onAction = {
                localFocusManager.moveFocus(FocusDirection.Down)
            },
        )
        BYTextInput.OutLinedTextField(
            label = R.string.subject_form_classRoom_label,
            keyboardOptions = KeyboardOptions(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
            ),
            onTextChange = {
                informationViewModel.setClassRoomName(it)
            },
            onAction = {
                localFocusManager.moveFocus(FocusDirection.Down)
            },
        )
        BYTextInput.OutLinedTextField(
            label = R.string.subject_form_credits_label,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
            onTextChange = {
                val credits = it.toIntOrNull()
                if (credits == null) {
                    informationViewModel.setCredits(
                        0,
                    )
                } else {
                    informationViewModel.setCredits(it.toInt())
                }
            },
            onAction = {
                localFocusManager.clearFocus()
            },
        )
        BYTextInput.OutLinedButtonTextField(
            label = R.string.subject_form_day_label,
            updateTextValue = { dayList },
            onClick = {
                localFocusManager.clearFocus()
                onShowSheet()
            },

        )

        Button(
            modifier = Modifier.fillMaxWidth(0.8f),
            onClick = {
                val payload = informationViewModel.createSubjectObject()
                onCreateSubjectClick(payload)
            },
            enabled = informationViewModel.isAllFieldsValid(),
        ) {
            Text(text = stringResource(id = R.string.subject_form_button_text), fontSize = 16.sp)
        }
    }
}
