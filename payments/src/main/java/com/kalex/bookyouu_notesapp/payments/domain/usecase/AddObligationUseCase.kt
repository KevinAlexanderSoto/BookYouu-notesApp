package com.kalex.bookyouu_notesapp.payments.domain.usecase

import android.content.Context
import com.kalex.bookyouu_notesapp.core.common.WidgetUpdateHelper
import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation
import com.kalex.bookyouu_notesapp.payments.domain.repository.ObligationRepository

class AddObligationUseCase(
    private val repository: ObligationRepository,
    private val context: Context
) {
    suspend operator fun invoke(obligation: Obligation): Int {
        val id = repository.addObligation(obligation)
        WidgetUpdateHelper.updateObligationsWidget(context)
        return id
    }
}
