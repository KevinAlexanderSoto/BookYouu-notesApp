package com.kalex.bookyouu_notesapp.investments.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import com.kalex.bookyouu_notesapp.core.common.composables.LabeledInput
import com.kalex.bookyouu_notesapp.investments.R
import com.kalex.bookyouu_notesapp.investments.presentation.InvestmentTermPreset

@Composable
fun InvestmentDaysSelector(
    label: String = stringResource(R.string.add_investment_term_label),
    selectedDays: String,
    onDaysSelected: (String) -> Unit
) {
    val presets = InvestmentTermPreset.entries
    val isNoTerm = selectedDays.isBlank()
    val numericDays = selectedDays.toIntOrNull()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            presets.forEach { preset ->
                val isSelected = if (preset == InvestmentTermPreset.NO_TERM) {
                    isNoTerm
                } else {
                    selectedDays == preset.valueString
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            onDaysSelected(preset.valueString)
                        },
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    border = if (isSelected) null else BorderStroke(1.dp, Color.LightGray)
                ) {
                    Text(
                        text = if (preset == InvestmentTermPreset.NO_TERM) {
                            stringResource(R.string.add_investment_term_no_term)
                        } else {
                            stringResource(R.string.add_investment_term_days_suffix, preset.valueString)
                        },
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 2.dp),
                        textAlign = TextAlign.Center,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val sliderValue = (numericDays ?: 1).coerceIn(1, 360).toFloat()

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (isNoTerm) stringResource(R.string.add_investment_duration_indefinite) else stringResource(R.string.add_investment_duration_days, selectedDays),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Slider(
            value = if (isNoTerm) 1f else sliderValue,
            onValueChange = { newValue ->
                onDaysSelected(newValue.toInt().toString())
            },
            valueRange = 1f..360f,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        LabeledInput(
            label = stringResource(R.string.add_investment_custom_days_label),
            value = if (isNoTerm) "" else selectedDays,
            onValueChange = { input ->
                onDaysSelected(input)
            },
            placeholder = if (isNoTerm) stringResource(R.string.add_investment_custom_days_placeholder_no_term) else stringResource(R.string.add_investment_custom_days_placeholder_active),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
    }
}
