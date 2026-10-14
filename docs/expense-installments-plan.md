# SDD & Business Plan: Expense Installments (Cuotas)

This document establishes the business definitions, user flows, and technical implementation plan for handling **Expenses with Installments (Cuotas)** in BookYouu.

---

## 1. 📊 Business Plan & Strategy

### 1.1 Objective
To allow users to register purchases made with installments (credit card or installment loans), ensuring each monthly installment automatically appears in subsequent months as a payment/expense for that specific month, without distorting the initial month's cash flow.

### 1.2 Core Business Definitions

#### Definition 1: Amount Semantics & Calculation
* When the user enters an amount in `AddExpenseScreen` (e.g., `$1,200.00`) and selects installments (e.g., `12`):
  * **Input Amount:** Represents the **Total Purchase Amount**.
  * **Installment Amount:** $\text{Cuota Amount} = \frac{\text{Total Amount}}{\text{Total Installments}}$.
  * **Rounding & Remainder:** To guarantee that the sum of all installments equals the exact total amount:
    $$\text{Base Amount} = \text{roundDown}(\text{Total} / N, 2)$$
    The last installment absorbs any leftover rounding difference:
    $$\text{Last Installment} = \text{Total} - (\text{Base Amount} \times (N - 1))$$
  * *Example:* $100.00 with 3 installments $\rightarrow$ Month 1: $33.33, Month 2: $33.33, Month 3: $33.34. Total = $100.00.

#### Definition 2: Timeline & Month Allocation
* **Installment 1 (Cuota 1):** Scheduled for the selected expense date (e.g., October 14, 2026).
* **Subsequent Installments (Cuota $k$, where $2 \le k \le N$):** Scheduled for Month $+ (k - 1)$ on the same day of the month.
* **Month-End Clamping:** If the purchase is on day 31 and the next month has fewer days (e.g., February 28/29 or April 30), `LocalDate.plusMonths(k - 1)` automatically clamps to the last valid day of that month.

#### Definition 3: Module Scope & Destination
* **Scope A: Future Monthly Expenses (`:expenses`) [Essential]**
  * When viewing the `ExpenseListScreen` in Month $k$, it displays only that month's installment: e.g., *"Laptop (Cuota 2/12) - $100.00"*.
  * The monthly summary card (`totalSpent`) reflects only the installment for that month, keeping monthly budgets accurate.
* **Scope B: Payment Obligations Integration (`:payments`) [Optional / Complementary]**
  * In BookYouu, the bottom navigation tab is **Payments / Pagos** (`:payments`).
  * Installments are financial obligations. An option can be provided to automatically sync or create a corresponding entry in `:payments` so the user can track it in their "Upcoming Payments" checklist and receive payment reminders.

#### Definition 4: Deletion & Editing Rules
* **Deleting an installment:**
  * Option 1: "Delete only this month's installment"
  * Option 2: "Delete this and all remaining future installments"
  * Option 3: "Delete entire series (all months)"
* **Editing an installment:**
  * Editing name/category updates the series.
  * Editing amount prompts whether to recalculate future installments.

---

## 2. 🏛️ Architecture & Data Layer (`:db`)

### 2.1 Database Schema Update (`ExpenseEntity.kt`)
Bump Room database version from `5` to `6`.

```kotlin
@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "month_year")
    val monthYear: String, // Format: "MM-YYYY"
    @ColumnInfo(name = "total_installments")
    val totalInstallments: Int? = null,
    @ColumnInfo(name = "current_installment")
    val currentInstallment: Int? = null,
    @ColumnInfo(name = "parent_expense_id")
    val parentExpenseId: Long? = null
)
```

### 2.2 DAO Methods (`ExpenseDao.kt`)
```kotlin
@Insert(onConflict = OnConflictStrategy.REPLACE)
suspend fun insertExpenses(expenses: List<ExpenseEntity>): List<Long>

@Query("DELETE FROM expenses WHERE parent_expense_id = :parentId OR id = :parentId")
suspend fun deleteExpenseSeries(parentId: Long)

@Query("DELETE FROM expenses WHERE (parent_expense_id = :parentId OR id = :parentId) AND current_installment >= :fromInstallment")
suspend fun deleteFutureInstallments(parentId: Long, fromInstallment: Int)

@Query("SELECT * FROM expenses WHERE parent_expense_id = :parentId OR id = :parentId ORDER BY current_installment ASC")
suspend fun getExpenseSeries(parentId: Long): List<ExpenseEntity>
```

---

## 3. 🎯 Domain Layer (`:expenses:domain`)

### 3.1 Domain Model (`Expense.kt`)
```kotlin
data class Expense(
    val id: Long = 0,
    val amount: Double,
    val description: String,
    val category: Category,
    val date: LocalDateTime,
    val monthYear: String, // "MM-YYYY"
    val totalInstallments: Int? = null,
    val currentInstallment: Int? = null,
    val parentExpenseId: Long? = null
)
```

### 3.2 Use Case: `AddExpenseUseCase.kt`
When saving an expense:
1. If `totalInstallments == null || totalInstallments <= 1`:
   - Save single expense entry as usual.
2. If `totalInstallments > 1`:
   - Calculate installment amounts with remainder distribution.
   - Insert the first installment (`currentInstallment = 1`, `parentExpenseId = null`).
   - Use the generated primary key `firstExpenseId` as `parentExpenseId` for installments $2 \dots N$.
   - Stagger dates using `firstDate.plusMonths(i - 1)`.
   - Batch insert all subsequent installments in a Room `@Transaction`.

---

## 4. 🎨 Presentation Layer (`:expenses:presentation`)

### 4.1 UI Enhancements in `AddExpenseScreen`
* Below the "Cuotas" TextField, display a real-time summary chip:
  * e.g., *"12 cuotas de $100.00 / mes (Total: $1,200.00)"*
* Clear guidance so the user understands they are entering the Total amount.

### 4.2 Display in `ExpenseRow`
* When `currentInstallment != null && totalInstallments != null`:
  * Show an installment pill badge: **`Cuota 2/12`** with category tint.
* In PDF Export (`MonthlyReportPdfExporter`): Include the installment indicator in the description or dedicated column.

---

## 5. 🔄 Implementation Phases

1. **Phase 1: Database Migration (`:db`)**
   - Update `ExpenseEntity` with `current_installment` and `parent_expense_id`.
   - Add DAO queries.
   - Configure Room migration (`5` to `6`).
2. **Phase 2: Domain Logic (`:expenses`)**
   - Update `Expense` domain model and mapper.
   - Refactor `AddExpenseUseCase` to generate installment series.
   - Add unit tests for installment division and remainder calculation.
3. **Phase 3: Presentation & UI (`:expenses`)**
   - Add installment summary preview in `AddExpenseScreen`.
   - Update `ExpenseRow` to display `Cuota X/Y`.
   - Add series deletion dialog (Delete Single vs Delete Series).
4. **Phase 4: Optional Payments Module Integration (`:payments`)**
   - If selected, automatically bridge installments to `ObligationEntity` or display in `Upcoming Payments`.
5. **Phase 5: Validation & Tests**
   - Execute `./gradlew test` and verify all tests pass.
