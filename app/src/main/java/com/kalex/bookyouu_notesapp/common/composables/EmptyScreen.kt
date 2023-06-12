package com.kalex.bookyouu_notesapp.common.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalex.bookyouu_notesapp.R

@Composable
fun EmptyScreen(
    onAddItemClick: () -> Unit,
    @StringRes rationaleText: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        modifier = Modifier.padding(20.dp).fillMaxSize(),
    ) {
        Icon(
            painterResource(R.drawable.circle_help_svgrepo_com),
            contentDescription = "No subject Icon",
            modifier = Modifier.size(80.dp),
        )
        Text(
            text = stringResource(rationaleText),
            fontSize = 20.sp,
        )
        Button(
            onClick = { onAddItemClick.invoke() },
            contentPadding = PaddingValues(20.dp, 10.dp),

            ) {
            Icon(
                painterResource(R.drawable.file_add_svgrepo_com),
                contentDescription = "Add button",
                modifier = Modifier.size(22.dp),
            )
            Text(text = stringResource(R.string.subject_list_no_subjectsFount_ButtonText), modifier = Modifier.padding(8.dp, 2.dp))
        }
    }
}
