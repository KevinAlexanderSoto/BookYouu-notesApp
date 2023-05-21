package com.kalex.bookyouu_notesapp.subject.createsubject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kalex.bookyouu_notesapp.R
import com.kalex.bookyouu_notesapp.ui.composables.BYTextInput

@Composable
fun ScaffoldContent(
    onShowSheet: () -> Unit,
    informationViewModel: SubjectFormInformationViewModel = hiltViewModel(),
) {
    val dayList = informationViewModel.getListOfStringSelectedDays().map {
        stringResource(id = it)
    }.toList().joinToString()
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
        BYTextInput.OutLinedButtonTextField(
            label = R.string.subject_form_credits_label, //TODO : add days label
            updateTextValue = { dayList },
            onClick = { onShowSheet() },

        )

        Button(onClick = {
            // TODO: Create subject
        }) {
            Text(text = "Create Subject")
        }
        // Add check for 7 days
    }
}
