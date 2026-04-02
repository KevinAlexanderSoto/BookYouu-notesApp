# 📑 Project Plan: Monthly Obligations Feature

This plan outlines the implementation of a new module, `:payments`, using **Clean Architecture**, **MVVM**, **Jetpack Compose**, **Room**, and **Koin**.

---

## 1. Business Plan & Strategy

### **Objective**
To provide a centralized financial dashboard that tracks recurring monthly expenses, reducing late payments and improving cash flow visibility.

### **Core Features**
* **Monthly Snapshot:** Real-time calculation of total, pending, and paid amounts.
* **Status Tracking:** One-tap interaction to mark obligations as completed.
* **Recurrence Management:** Automatic reset of payment statuses at the start of each month.

### **Key Metrics (KPIs)**
* **Completion Rate:** Percentage of obligations marked as "Paid" before their due date.
* **Financial Awareness:** Frequency of dashboard views relative to total app sessions.

---

## 2. Phase 1: Model Layer (Data)

Focus on persistence and the "Source of Truth."

### **2.1 Room Entity**
The entity tracks the current state and the specific day of the month the payment is due.
```kotlin
 @Entity(tableName = "obligations")
data class ObligationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val amount: Double,
    val dayOfMonth: Int, // 1 - 31
    val category: String,
    val isPaid: Boolean = false,
    val lastPaidDate: Long? = null // Timestamp for "Paid Oct 02"
)
```

### **2.2 Room DAO**
Reactive queries ensure the UI updates instantly when a payment is toggled.
```kotlin
interface ObligationDao {
    @Query("SELECT * FROM obligations ORDER BY isPaid ASC, dayOfMonth ASC")
    fun getMonthlyObligations(): Flow<List<ObligationEntity>>

    @Update
    suspend fun updateObligation(obligation: ObligationEntity)
    
    @Insert
    suspend fun insertObligation(obligation: ObligationEntity)
}
```

---

## 3. Phase 2: Domain Layer

Business logic abstraction to decouple the UI from the database.

### **3.1 Repository Interface**
```kotlin
interface ObligationRepository {
    fun getObligations(): Flow<List<ObligationEntity>>
    suspend fun togglePaymentStatus(obligation: ObligationEntity)
}
```

### **3.2 Use Cases**
* **`GetObligationsUseCase`**: Aggregates the list into a summary model for the top card.
* **`TogglePaymentUseCase`**: Logic to flip `isPaid` and update the `lastPaidDate`.

---

## 4. Phase 3: Presentation Layer (MVVM)

### **4.1 UI State**
Represent the UI as a single immutable state.
```kotlin
data class ObligationsUiState(
    val totalBalance: Double = 0.0,
    val pendingAmount: Double = 0.0,
    val paidAmount: Double = 0.0,
    val obligations: List<ObligationEntity> = emptyList(),
    val isLoading: Boolean = false
)
```

### **4.2 ViewModel**
Calculates the summary totals based on the stream from the repository.
```kotlin
class ObligationsViewModel(
    private val repository: ObligationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ObligationsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        repository.getObligations().onEach { list ->
            _uiState.update { it.copy(
                obligations = list,
                totalBalance = list.sumOf { it.amount },
                pendingAmount = list.filter { !it.isPaid }.sumOf { it.amount },
                paidAmount = list.filter { it.isPaid }.sumOf { it.amount }
            )}
        }.launchIn(viewModelScope)
    }

    fun onPaymentToggled(item: ObligationEntity) {
        viewModelScope.launch { repository.togglePaymentStatus(item) }
    }
}
```

---

## 5. Phase 4: UI Layer (Compose)

### **5.1 Summary Card (`OverviewCard`)**
* **Header:** Large bold text for the Current Balance ($B$).
* **Logic:** $B = Pending + Paid$.
* **Dividers:** Use `HorizontalDivider` and vertical separators between Pending and Paid values to match the design.

### **5.2 List Item (`ObligationRow`)**
* **Pending State:** Full opacity, red/orange "PENDING" badge.
* **Paid State:** $0.6f$ alpha on text, green checkmark icon, green "PAID" badge.
* **Formatting:** Use `NumberFormat.getCurrencyInstance()` with the `es-CO` locale for COP formatting ($4.850,00).

---

## 6. Phase 5: DI & Infrastructure

### **6.1 Koin Module**
```kotlin
val paymentsModule = module {
    single { get<AppDatabase>().obligationDao() }
    single<ObligationRepository> { ObligationRepositoryImpl(get()) }
    viewModel { ObligationsViewModel(get()) }
}
```

### **6.2 Monthly Reset (WorkManager)**
To handle the "Every Month" requirement:
1.  Create a `MonthlyResetWorker`.
2.  Schedule it to run on the 1st day of every month.
3.  Logic: `UPDATE obligations SET isPaid = 0, lastPaidDate = NULL`.

---

## 🛠 Technical Considerations
* **Multi-Module:** Ensure `:payments` depends on `:db` and `:core` to share common styles and DB instances.
* **Navigation:** Register the entry point in your `NavHost` using a typed route or string constant.
* **State Hoisting:** Keep the `ObligationRow` stateless by passing the `onToggle` lambda up to the ViewModel.
