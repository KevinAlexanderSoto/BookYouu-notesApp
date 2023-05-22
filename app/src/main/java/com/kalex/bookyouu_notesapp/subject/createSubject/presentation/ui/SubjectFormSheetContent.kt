package com.kalex.bookyouu_notesapp.subject.createSubject.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kalex.bookyouu_notesapp.subject.createSubject.DayOfWeekStringFactory
import com.kalex.bookyouu_notesapp.ui.composables.BYRoundedCheckView
import java.time.DayOfWeek

@Composable
fun SheetContent(
    onHideClick: () -> Unit,
    onOptionSelected: (DayOfWeek) -> Unit,
    onOptionNotSelected: (DayOfWeek) -> Unit,
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
            Text(
                "Select the days of class",//TODO: add strings resources
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { onHideClick.invoke() }) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close",
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(20.dp, 40.dp),
            content = {
                items(DayOfWeek.values().size) {
                    BYRoundedCheckView(
                        text = DayOfWeekStringFactory.getDayStringResource(DayOfWeek.values()[it]),
                        isRoundedChecked = { isCheck ->
                            if (isCheck) {
                                onOptionSelected(DayOfWeek.values()[it])
                            } else {
                                onOptionNotSelected(
                                    DayOfWeek.values()[it],
                                )
                            }
                        },
                    )
                }
            },
        )
    }
}

