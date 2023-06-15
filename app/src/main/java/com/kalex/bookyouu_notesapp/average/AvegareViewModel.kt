package com.kalex.bookyouu_notesapp.average

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AverageViewModel : ViewModel() {
    private val _averageState =
        MutableStateFlow<AverageResult>(AverageResult(0.0, 0.0))
    val averageState: StateFlow<AverageResult>
        get() = _averageState.asStateFlow()

    fun calculateAverage(
        itemsCount: Int,
        percentageMap: Map<Int, Double?>,
        ratingsMap: Map<Int, Double?>,
    ) {
        val newPercentageMap: Map<Int, Double> = percentageMap.mapValues { entry ->
            entry.value?.div(100.0) ?: 0.0
        }
        val ratingsPercentageList = mutableListOf<Double>()
        for (i in 0 until itemsCount) {
            ratingsPercentageList.add(ratingsMap[i]?.times(newPercentageMap[i] ?: 0.0) ?: 0.0)
        }
        _averageState.value = AverageResult(
            ratingsPercentageList.sum(),
            newPercentageMap.values.sum().times(100.0),
        )
    }
}

data class AverageResult(
    val average: Double,
    val percentage: Double,
)
