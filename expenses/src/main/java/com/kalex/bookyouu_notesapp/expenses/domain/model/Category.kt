package com.kalex.bookyouu_notesapp.expenses.domain.model

enum class Category(val id: Int, val displayName: String) {
    FOOD(1, "Food"),
    HEALTH(2, "Health"),
    EDUCATION(3, "Education"),
    LEISURE(4, "Entertainment"),
    TRANSPORT(5, "Transport"),
    HOME(6, "Home"),
    SHOPPING(7, "Shopping"),
    OTHERS(8, "Others")
}
