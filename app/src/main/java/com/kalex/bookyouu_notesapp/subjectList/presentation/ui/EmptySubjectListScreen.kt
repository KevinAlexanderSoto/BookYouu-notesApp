package com.kalex.bookyouu_notesapp.subjectList.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kalex.bookyouu_notesapp.R

@Composable
fun EmptySubjectScreen(
    onCreateSubjectClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp),
    ) {
        Icon(
            Icons.Default.Notifications,
            contentDescription = "No subject Icon",
            modifier = Modifier.size(60.dp),
        )
        Text(text = stringResource(R.string.subjectList_no_subjectsFount_text))
        Button(onClick = { onCreateSubjectClick.invoke() }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add button",
            )
            Text(text = stringResource(R.string.subjectList_no_subjectsFount_ButtonText))
        }
    }
}

@Preview
@Composable
fun PreviewView() {
    EmptySubjectScreen() {
    }
}
