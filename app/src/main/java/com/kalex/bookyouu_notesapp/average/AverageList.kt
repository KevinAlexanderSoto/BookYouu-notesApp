package com.kalex.bookyouu_notesapp.average

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kalex.bookyouu_notesapp.R
import java.math.RoundingMode
import kotlin.math.roundToInt

@Composable
fun AverageList(
    paddingValues: PaddingValues,
    averageViewModel: AverageViewModel = hiltViewModel(),
) {
    var addItem by remember {
        mutableStateOf(3)
    }
    val averagePercentageData = remember {
        mutableMapOf<Int, Double?>()
    }
    val averageRatingData = remember {
        mutableMapOf<Int, Double?>()
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
    ) {
        Box(
            Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
        ) {
            IconButton(
                onClick = {
                    if (addItem > 0) {
                        val actualMapKey = addItem - 1
                        averagePercentageData.remove(actualMapKey)
                        averageRatingData.remove(actualMapKey)
                        addItem--
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 20.dp),

            ) {
                Icon(
                    painterResource(id = R.drawable.remove_24px),
                    contentDescription = "add new",
                    tint = Color.Black,
                )
            }
            Text(
                text = stringResource(id = R.string.average_title),
                modifier = Modifier.align(Alignment.Center),
                fontSize = 24.sp,
            )
            IconButton(
                onClick = { addItem++ },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp),

            ) {
                Icon(Icons.Default.Add, contentDescription = "add new")
            }
        }
        var showAverageResult by remember { mutableStateOf(false) }
        AnimatedVisibility(visible = showAverageResult) {
            val average =
                averageViewModel.averageState.collectAsStateWithLifecycle().value.average
            val percentage =
                averageViewModel.averageState.collectAsStateWithLifecycle().value.percentage
            val args = listOf((Math.round(percentage * 1000.0) / 1000.0).toString(), (Math.round(average * 1000.0) / 1000.0).toString()).toTypedArray()

            AlertDialog(
                title = { Text(text = stringResource(id = R.string.average_dialog_title)) },
                text = {
                    Column() {
                        Text(
                            fontSize = 20.sp,
                            text = if (percentage.toDouble() > 100.0) {
                                stringResource(id = R.string.average_percentage_error_text)
                            } else {
                                stringResource(id = R.string.average_percentage_average_text, *args)
                            },
                        )
                    }
                },
                onDismissRequest = { showAverageResult = false },
                confirmButton = {
                    TextButton(onClick = {
                        showAverageResult = false
                    }) {
                        Text(
                            text = stringResource(id = R.string.average_confirmButton_text),
                            fontSize = 18.sp,
                        )
                    }
                },
            )
        }
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(addItem) { item ->
                averagePercentageData.put(item, 0.0)
                averageRatingData.put(item, 0.0)
                AverageItem(
                    itemNumber = item + 1,
                    onPercentageChange = {
                        if (it == "") {
                            averagePercentageData[item] = 0.0
                        } else {
                            averagePercentageData[item] = it.toDoubleOrNull()
                        }
                    },
                    onRatingChange = {
                        if (it == "") {
                            averageRatingData[item] = 0.0
                        } else {
                            averageRatingData[item] = it.toDoubleOrNull()
                        }
                    },
                )
            }
            item() {
                Button(
                    modifier = Modifier.fillMaxWidth(0.8f).padding(top = 10.dp),
                    onClick = {
                        averageViewModel.calculateAverage(
                            itemsCount = addItem,
                            percentageMap = averagePercentageData,
                            ratingsMap = averageRatingData,
                        )
                        showAverageResult = true
                    },
                ) {
                    Text(
                        text = stringResource(id = R.string.average_calculate_button_text),
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}
