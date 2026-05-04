package com.kalex.bookyouu_notesapp.payments.domain.usecase

import android.content.Context
import com.kalex.bookyouu_notesapp.core.common.WidgetUpdateHelper
import com.kalex.bookyouu_notesapp.payments.domain.repository.ObligationRepository

class DeleteObligationUseCase(
    private val repository: ObligationRepository,
    private val context: Context
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteObligation(id)
        WidgetUpdateHelper.updateObligationsWidget(context)
    }

    suspend operator fun invoke(ids: List<Int>) {
        repository.deleteObligations(ids)
        WidgetUpdateHelper.updateObligationsWidget(context)
    }
}
