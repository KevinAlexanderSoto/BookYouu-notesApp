# Implementation Plan: Investments & Savings Feature

This document outlines the strategy for implementing the new Investments feature based on the Stitch UI/UX design. The implementation will follow the project's Dark Green aesthetic (#004D40) and multi-module MVI architecture.

## 🎨 Design Standards
- **Primary Color:** `#004D40` (Dark Green)
- **Secondary Color:** `#2D5D57`
- **Surface:** White/Light Grey with 16dp rounded corners.
- **Typography:** Material 3 (Headline Medium for amounts, Label Small for badges).

---

## 🏗️ Stage 1: Portfolio Overview Screen
**Goal:** Create the main landing screen for investments.

- **Composables:**
  - `NetWorthCard`: Displays total balance, 30-day growth percentage, and a progress indicator.
  - `InvestmentBucketItem`: Custom list item with styled icons, name, balance, and "Percentage of Total" badges.
  - `PortfolioHeader`: Modern header with notification and profile actions.
- **Components to reuse:** `EmptyScreen` from `:core` if no investments exist.
- **Logic:** Calculate total net worth by summing all `InvestmentEntity` balances + `initialAmount`.

## 📈 Stage 2: Investment Detail & Activity Screen
**Goal:** Provide deep insights into a specific investment.

- **Composables:**
  - `PerformanceChart`: A bar chart representing investment growth (using Canvas or a lightweight library if available, otherwise stylized shapes).
  - `StatsRow`: Annual Return and Risk Level info cards.
  - `TransactionItem`: History list showing Deposits, Withdrawals, and Dividends with corresponding icons.
  - `ActionFooter`: "Add Funds" (Primary) and "Withdraw" (Secondary) buttons.
- **Logic:** Fetch `InvestmentTransactionEntity` filtered by `investmentId`.

## ➕ Stage 3: Add Investment Screen
**Goal:** A user-friendly form to create new investment buckets.

- **Composables:**
  - `InitialAmountInput`: Large display for the starting balance.
  - `InvestmentForm`: Fields for Name, Category (Stocks, Savings, Crypto), Term, and Annual Revenue.
  - `CustomNumericKeypad`: A grid-based keypad for quick amount entry.
  - `CreateButton`: Styled button with "Create Investment ->" text.
- **Components to reuse:** `LabeledInput` for the name and percentage fields.

## 🗺️ Stage 4: Navigation & Bottom Bar Integration
**Goal:** Integrate the feature into the app's main flow.

- **Navigation:**
  - Implement `InvestmentsNavigationGraph.kt` in the `:app` module.
  - Define transitions (Slide in/out) between Overview and Detail.
- **Bottom Bar:**
  - Update `BottomNavigationScreens.kt` to include the `Investments` item.
  - Add the `show_chart` or `trending_up` icon to the bottom bar.

## ⚙️ Stage 5: MVI & Data Wiring
**Goal:** Connect the UI to the database via ViewModels.

- **MVI Setup:**
  - Define `InvestmentsState`, `InvestmentsAction`, and `InvestmentsEvent`.
  - Implement `InvestmentListViewModel` and `InvestmentDetailViewModel`.
- **Use Cases:**
  - `CalculatePortfolioStatsUseCase`: To handle the complex math for growth and allocation percentages.
- **Validation:**
  - Unit tests for the stats calculation logic.
  - Integration tests for the transaction flow (ensuring withdrawals subtract correctly).
