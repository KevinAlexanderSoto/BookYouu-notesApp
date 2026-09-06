package com.kalex.bookyouu_notesapp.investments.presentation

import com.kalex.bookyouu_notesapp.investments.domain.model.Investment
import com.kalex.bookyouu_notesapp.investments.domain.repository.InvestmentsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InvestmentListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeInvestmentsRepository
    private lateinit var viewModel: InvestmentListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeInvestmentsRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadPortfolio maps investments correctly with appropriate formatted values`() = runTest {
        val sampleInvestment = Investment(
            id = 1L,
            name = "My Savings",
            type = InvestmentType.USD,
            initialAmount = 5656.0,
            currency = "USD",
            dateCreated = 1700000000000L
        )
        fakeRepository.investments = listOf(sampleInvestment)

        viewModel = InvestmentListViewModel(fakeRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("$ 5,656", state.totalNetWorth)
        assertEquals(1, state.investments.size)
        assertEquals("MY SAVINGS", state.investments[0].name.uppercase())
        assertEquals(InvestmentType.USD, state.investments[0].type)
    }

    private class FakeInvestmentsRepository : InvestmentsRepository {
        var investments: List<Investment> = emptyList()

        override fun getInvestments(): Flow<List<Investment>> = flowOf(investments)

        override fun getInvestmentById(id: Long): Flow<Investment> = flowOf(
            investments.first { it.id == id }
        )

        override suspend fun upsertInvestment(investment: Investment) {
            investments = investments + investment
        }

        override suspend fun deleteInvestment(investment: Investment) {
            investments = investments.filterNot { it.id == investment.id }
        }
    }
}
