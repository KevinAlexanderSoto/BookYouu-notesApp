# Implementation Plan: Monthly Obligation Creation

This plan integrates the "Add Obligation" feature into the `:payments` module using **MVI + Clean Architecture + Koin**.

---

## 🏗️ Phase 1: Presentation Layer (MVI & UI)
Focus on UI components and state management for `ObligationsCreateScreen`.

### 1. Define MVI Contract (`ObligationsCreateState.kt`)
*   **State:** `name`, `amount`, `frequency`, `dayOfMonth`, `category`, `isLoading`, `isSaveEnabled`.
*   **Actions (Intents):** `OnNameChange`, `OnAmountChange`, `OnFrequencyChange`, `OnDayChange`, `OnCategoryChange`, `OnSaveClick`.
*   **Events (Side Effects):** `ObligationSaved` (Navigate Back), `ShowError(UiText)`.

### 2. Reusable UI Components (`:payments:presentation:components`)
*   **`LabeledInput`:** Custom TextField with an overline label and bottom divider.
*   **`PaymentFrequencyCard`:** A container holding:
    *   **`SegmentedToggleButton`:** Horizontal selector for `Monthly`, `Weekly`, `Bi-weekly`.
    *   **`DaySelector`:** Horizontal `LazyRow` of selectable days (1-7 or 1-31 depending on frequency).
*   **`CategoryGrid`:** A grid of `CategoryCard` items (Icon + Text) using `LazyVerticalGrid`.

### 3. ViewModel (`ObligationsCreateViewModel.kt`)
*   Hoist state using `MutableStateFlow<ObligationsCreateState>`.
*   Handle user input validation (e.g., amount must be a valid number, name cannot be empty).
*   Inject `AddObligationUseCase` via constructor.
*   Expose `onAction(action: ObligationsCreateAction)` to the UI.

### 4. Screen Assembly (`ObligationsCreateScreen.kt`)
*   **`ObligationsCreateRoot`:** Inject ViewModel via `koinViewModel()`, observe events using `ObserveAsEvents`.
*   **`ObligationsCreateScreen`:** Stateless Composable following the mockup design. Use a `Scaffold` with `ScaffoldTopBar` and a sticky `Save Obligation` button at the bottom.

---

## 🏛️ Phase 2: Domain Layer (Business Logic)
Establish the data structures and rules for obligations.

### 1. Update Domain Model (`Obligation.kt`)
*   Add `frequency: PaymentFrequency` field.
*   Define Enums:
    *   `PaymentFrequency`: `MONTHLY`, `WEEKLY`, `BI_WEEKLY`.
    *   `ObligationCategory`: `HOUSE`, `INTERNET`, `GYM`, `UTILITY`, `FOOD`, `TRANSPORT`.

### 2. Create Use Case (`AddObligationUseCase.kt`)
*   Encapsulate the logic for creating a new `Obligation` and saving it via the repository.
*   Return a `Result<Unit, DataError.Local>` for consistent error handling.

---

## 💾 Phase 3: Data Layer (Persistence)
Handle Room entity updates and repository implementation.

### 1. Update Room Entity (`ObligationEntity.kt`)
*   Add `@ColumnInfo(name = "frequency") val frequency: String`.
*   *Note: Requires a Room Migration (Schema change).*

### 2. Update Mapper (`ObligationMapper.kt`)
*   Map the new `frequency` field between the domain model (Enum) and the entity (String).

### 3. Repository Implementation (`RoomObligationRepository.kt`)
*   Implement `addObligation` to map the domain model to an entity and call `dao.insertObligation()`.

---

## ⚡ Phase 4: Dependency Injection & Wiring
*   Register `AddObligationUseCase` in `paymentsDomainModule`.
*   Register `ObligationsCreateViewModel` in `paymentsPresentationModule`.
*   Wire the navigation to `ObligationsCreateRoot` in the main app navigation graph.
