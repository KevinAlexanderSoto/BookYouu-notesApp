package com.kalex.bookyouu_notesapp.investments.data.repository

import com.kalex.bookyouu_notesapp.db.dao.InvestmentDao
import com.kalex.bookyouu_notesapp.investments.data.mapper.toDomain
import com.kalex.bookyouu_notesapp.investments.data.mapper.toEntity
import com.kalex.bookyouu_notesapp.investments.domain.model.Investment
import com.kalex.bookyouu_notesapp.investments.domain.repository.InvestmentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InvestmentsRepositoryImpl(
    private val investmentDao: InvestmentDao,
) : InvestmentsRepository {

    override fun getInvestments(): Flow<List<Investment>> {
        return investmentDao.getInvestments().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getInvestmentById(id: Long): Flow<Investment> {
        return investmentDao.getInvestmentById(id).map { it.toDomain() }
    }

    override suspend fun upsertInvestment(investment: Investment) {
        investmentDao.upsertInvestment(investment.toEntity())
    }

    override suspend fun deleteInvestment(investment: Investment) {
        investmentDao.deleteInvestment(investment.toEntity())
    }

}
