package com.kalex.bookyouu_notesapp.expenses.domain.model

enum class Category(val id: Int, val displayName: String) {
    FOOD(1, "Food"),
    TRANSPORT(2, "Transport"),
    HEALTH(3, "Health"),
    LEISURE(4, "Leisure"),
    OTHERS(5, "Others")
}
