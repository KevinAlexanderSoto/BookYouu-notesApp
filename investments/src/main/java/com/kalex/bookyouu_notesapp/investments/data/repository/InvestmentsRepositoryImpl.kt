package com.kalex.bookyouu_notesapp.investments.data.repository

import com.kalex.bookyouu_notesapp.db.dao.InvestmentDao
import com.kalex.bookyouu_notesapp.db.dao.InvestmentTransactionDao
import com.kalex.bookyouu_notesapp.investments.data.mapper.toDomain
import com.kalex.bookyouu_notesapp.investments.data.mapper.toEntity
import com.kalex.bookyouu_notesapp.investments.domain.model.Investment
import com.kalex.bookyouu_notesapp.investments.domain.model.InvestmentTransaction
import com.kalex.bookyouu_notesapp.investments.domain.repository.InvestmentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InvestmentsRepositoryImpl(
    private val investmentDao: InvestmentDao,
    private val transactionDao: InvestmentTransactionDao
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

    override fun getTransactionsForInvestment(investmentId: Long): Flow<List<InvestmentTransaction>> {
        return transactionDao.getTransactionsForInvestment(investmentId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun upsertTransaction(transaction: InvestmentTransaction) {
        transactionDao.upsertTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transaction: InvestmentTransaction) {
        transactionDao.deleteTransaction(transaction.toEntity())
    }
}
