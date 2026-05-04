package com.kalex.bookyouu_notesapp.core.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kalex.bookyouu_notesapp.core.R

sealed class Category(
    val id: Int,
    @StringRes val displayNameRes: Int,
    val icon: CategoryIcon
) {
    object FOOD : Category(1, R.string.category_food, CategoryIcon.Resource(R.drawable.fork_spoon_24dp))
    object HEALTH : Category(2, R.string.category_health, CategoryIcon.Resource(R.drawable.medical_services_24dp))
    object EDUCATION : Category(3, R.string.category_education, CategoryIcon.Resource(R.drawable.school_24dp))
    object ENTERTAINMENT : Category(4, R.string.category_entertainment, CategoryIcon.Resource(R.drawable.mood_24dp))
    object TRANSPORT : Category(5, R.string.category_transport, CategoryIcon.Resource(R.drawable.outline_directions_car_24))
    object HOME : Category(6, R.string.category_home, CategoryIcon.Resource(R.drawable.outline_home_24))
    object SHOPPING : Category(7, R.string.category_shopping, CategoryIcon.Resource(R.drawable.outline_shopping_cart_24))
    object GYM : Category(8, R.string.category_gym, CategoryIcon.Resource(R.drawable.outline_exercise_24))
    object SUBSCRIPTION : Category(9, R.string.category_subscription, CategoryIcon.Resource(R.drawable.outline_subscriptions_24))
    object UTILITY : Category(10, R.string.category_utility, CategoryIcon.Resource(R.drawable.outline_service_toolbox_24))
    object GENERAL : Category(11, R.string.category_general, CategoryIcon.Resource(R.drawable.outline_apps_24))
    object OTHERS : Category(12, R.string.category_others, CategoryIcon.Resource(R.drawable.outline_stacks_24))

    companion object {
        fun values() = listOf(
            FOOD, HEALTH, EDUCATION, ENTERTAINMENT, TRANSPORT, HOME, SHOPPING, GYM, SUBSCRIPTION, UTILITY, GENERAL, OTHERS
        )

        fun fromId(id: Int) = values().find { it.id == id } ?: OTHERS
        
        fun fromName(name: String) = values().find { it.name().equals(name, ignoreCase = true) } ?: OTHERS
    }

    fun name(): String = this::class.simpleName ?: "OTHERS"
}

sealed class CategoryIcon {
    data class Resource(@DrawableRes val resId: Int) : CategoryIcon()
}
