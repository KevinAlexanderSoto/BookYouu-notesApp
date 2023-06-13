package com.kalex.bookyouu_notesapp.subject.subjectList.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SubjectItem(
    title: String,
    subTitle: String,
    classRoom: String,
    onSubjectItemClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(24.dp, 16.dp)
            .fillMaxWidth()
            .height(78.dp)
            .clickable { onSubjectItemClick.invoke() },
    ) {
        Column(
            modifier = Modifier.align(
                Alignment.CenterStart,
            ).fillMaxWidth(0.70f),
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(2.dp, 4.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = subTitle,
                modifier = Modifier.padding(2.dp),
                maxLines = 1,
                fontSize = 14.sp,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 36.dp, bottom = 10.dp)
                .fillMaxWidth(0.2f),
        ) {
            Text(
                text = classRoom,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.Start),
            )
        }

        Icon(
            Icons.Default.KeyboardArrowRight,
            contentDescription = "list detail",
            modifier = Modifier.align(Alignment.CenterEnd),
        )
        Divider(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            thickness = 1.dp,
            modifier = Modifier.padding(4.dp).align(Alignment.BottomCenter),
        )
    }
}
