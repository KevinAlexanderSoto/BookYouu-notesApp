package com.kalex.bookyouu_notesapp.journal.createJournal.presentation.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalex.bookyouu_notesapp.journal.R
import com.kalex.bookyouu_notesapp.core.common.composables.BYTextInput
import com.kalex.bookyouu_notesapp.db.data.Journal
import com.kalex.bookyouu_notesapp.journal.createJournal.presentation.JournalFormInformationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun JournalFormScaffoldContent(
    paddingValues: PaddingValues,
    onShowSheet: () -> Unit,
    onCreateJournalClick: (Journal) -> Unit,
    informationViewModel: JournalFormInformationViewModel = koinViewModel(),
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

        Text(text = stringResource(id = R.string.journal_form_title))
        BYTextInput.OutLinedTextField(
            label = R.string.journal_form_name_label,
            keyboardOptions = KeyboardOptions(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
            ),
            onTextChange = {
                informationViewModel.setJournalName(it)
            },
            onAction = {
                localFocusManager.moveFocus(FocusDirection.Down)
            },
        )
        BYTextInput.OutLinedTextField(
            label = R.string.journal_form_location_label,
            keyboardOptions = KeyboardOptions(
                autoCorrect = true,
                keyboardType = KeyboardType.Text,
            ),
            onTextChange = {
                informationViewModel.setLocation(it)
            },
            onAction = {
                localFocusManager.moveFocus(FocusDirection.Down)
            },
        )
        BYTextInput.OutLinedTextField(
            label = R.string.journal_form_priority_label,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
            onTextChange = {
                val priority = it.toIntOrNull()
                if (priority == null) {
                    informationViewModel.setPriority(0)
                } else {
                    informationViewModel.setPriority(it.toInt())
                }
            },
            onAction = {
                localFocusManager.clearFocus()
            },
        )
        BYTextInput.OutLinedButtonTextField(
            label = R.string.journal_form_day_label,
            updateTextValue = { dayList },
            onClick = {
                localFocusManager.clearFocus()
                onShowSheet()
            },

        )

        Button(
            modifier = Modifier.fillMaxWidth(0.8f),
            onClick = {
                val payload = informationViewModel.createJournalObject()
                onCreateJournalClick(payload)
            },
            enabled = informationViewModel.isAllFieldsValid(),
        ) {
            Text(text = stringResource(id = R.string.journal_form_button_text), fontSize = 16.sp)
        }
    }
}
