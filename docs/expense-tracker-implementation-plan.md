# SDD: Expense Tracker Implementation Plan

This document outlines the technical design and implementation steps for the **Expense Tracker** feature in the BookYouu app. This feature focuses on tracking daily variable expenses, complementing the existing `:payments` module.

---

## 🏗️ Architecture Overview
- **Module:** `:expenses`
- **Pattern:** MVI (Model-View-Intent)
- **Frameworks:** Jetpack Compose, Koin (DI), Room (Persistence), Coroutines/Flow.
- **Skill Alignment:** Follows `android-presentation-mvi` and project-specific standards.

---

## 📅 Phase 1: Data & Infrastructure (`:db`)
*Goal: Establish the storage layer for expenses.*

1. **Entity Definition (`ExpenseEntity.kt`):**
    - `id: Long` (Primary Key, AutoGenerate)
    - `amount: Double`
    - `description: String`
    - `categoryId: Int`
    - `date: Long` (Timestamp)
    - `monthYear: String` (Format: "MM-YYYY" for optimized filtering)

2. **DAO Implementation (`ExpenseDao.kt`):**
    - `insertExpense(expense: ExpenseEntity)`
    - `deleteExpense(id: Long)`
    - `getExpensesByMonth(monthYear: String): Flow<List<ExpenseEntity>>`

3. **Database Integration:**
    - Register `ExpenseEntity` in `BookYouuDataBase.kt`.
    - Add `expenseDao()` to the database abstract class.
    - *Note: Requires a database version bump.*

---

## 🏛️ Phase 2: Domain Layer (`:expenses:domain`)
*Goal: Define business rules and use cases.*

1. **Models:**
    - `Expense`: Domain representation.
    - `Category`: Enum for expense categories (Food, Transport, Health, Leisure, etc.).
    - `MonthlySummary`: Aggregated data for the UI.

2. **Repository Interface:**
    - `ExpenseRepository`: Define operations for persistence.

3. **Use Cases:**
    - `GetMonthlyExpensesUseCase`: Retrieve and sort expenses.
    - `GetMonthlySummaryUseCase`: Calculate total spent and category percentages.
    - `AddExpenseUseCase`: Validate amount and save via repository.

---

## ⚡ Phase 3: Presentation Layer (`:expenses:presentation`)
*Goal: Implement the state machine and logic.*

1. **MVI Contract:**
    - **`ExpenseState`:** `expenses: List<ExpenseUi>`, `totalSpent: String`, `selectedMonth: YearMonth`, `isLoading: Boolean`, `error: UiText?`.
    - **`ExpenseAction`:** `OnMonthChange(newMonth)`, `OnDeleteExpense(id)`, `OnAddExpenseClick`, `OnSaveExpense(amount, desc, category)`.
    - **`ExpenseEvent`:** `NavigateToAddExpense`, `ShowSnackbar(message)`, `ExpenseSaved`.

2. **ViewModel (`ExpenseViewModel.kt`):**
    - Use `MutableStateFlow` with `.update { }`.
    - Use `Channel` for side-effect events.
    - Use `SavedStateHandle` to survive process death for the `selectedMonth`.

3. **Dependency Injection:**
    - Register `expensesModule` in Koin.

---

## 🎨 Phase 4: UI Development (`:expenses:presentation:ui`)
*Goal: Build the screens based on the designs.*

1. **Reusable Components:**
    - `ExpenseRow`: Individual item with `animateItem()` and `SwipeToDismissBox`.
    - `ExpenseSummaryCard`: Visual breakdown (Card with `#004D40` background).
    - `CategorySelector`: Grid of icons for expense classification.

2. **Screens:**
    - **`ExpenseListRoot/Screen`:** List of expenses with month navigation.
    - **`AddExpenseRoot/Screen`:** Bottom sheet or full screen for entry.
    - Use `LabeledInput` with `NumberVisualTransformation` for currency.

---

## 🔗 Phase 5: Integration & Final Polish
*Goal: Connect to the app and refine.*

1. **Navigation:** Add routes to the `:app` module's navigation graph.
2. **Localization:** Add strings to `strings.xml` (ES/EN).
3. **Themes:** Ensure consistent use of `MaterialTheme.colorScheme.primary` (#004D40).
4. **Testing:** Unit tests for UseCases and ViewModels.
