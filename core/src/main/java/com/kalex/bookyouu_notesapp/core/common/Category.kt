package com.kalex.bookyouu_notesapp.core.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.kalex.bookyouu_notesapp.core.R

sealed class Category(
    val id: Int,
    @StringRes val displayNameRes: Int,
    val icon: CategoryIcon,
    val backgroundColor: Color = Color(0xFFECEFF1),
    val iconColor: Color = Color(0xFF37474F)
) {
    object FOOD : Category(
        id = 1,
        displayNameRes = R.string.category_food,
        icon = CategoryIcon.Resource(R.drawable.fork_spoon_24dp),
        backgroundColor = Color(0xFFFFF3E0),
        iconColor = Color(0xFFE65100)
    )
    object HEALTH : Category(
        id = 2,
        displayNameRes = R.string.category_health,
        icon = CategoryIcon.Resource(R.drawable.medical_services_24dp),
        backgroundColor = Color(0xFFFFEBEE),
        iconColor = Color(0xFFC62828)
    )
    object EDUCATION : Category(
        id = 3,
        displayNameRes = R.string.category_education,
        icon = CategoryIcon.Resource(R.drawable.school_24dp),
        backgroundColor = Color(0xFFE8EAF6),
        iconColor = Color(0xFF283593)
    )
    object ENTERTAINMENT : Category(
        id = 4,
        displayNameRes = R.string.category_entertainment,
        icon = CategoryIcon.Resource(R.drawable.mood_24dp),
        backgroundColor = Color(0xFFEDE7F6),
        iconColor = Color(0xFF6A1B9A)
    )
    object TRANSPORT : Category(
        id = 5,
        displayNameRes = R.string.category_transport,
        icon = CategoryIcon.Resource(R.drawable.outline_directions_car_24),
        backgroundColor = Color(0xFFE3F2FD),
        iconColor = Color(0xFF1565C0)
    )
    object HOME : Category(
        id = 6,
        displayNameRes = R.string.category_home,
        icon = CategoryIcon.Resource(R.drawable.outline_home_24),
        backgroundColor = Color(0xFFE0F2F1),
        iconColor = Color(0xFF00695C)
    )
    object SHOPPING : Category(
        id = 7,
        displayNameRes = R.string.category_shopping,
        icon = CategoryIcon.Resource(R.drawable.outline_shopping_cart_24),
        backgroundColor = Color(0xFFFCE4EC),
        iconColor = Color(0xFFC2185B)
    )
    object GYM : Category(
        id = 8,
        displayNameRes = R.string.category_gym,
        icon = CategoryIcon.Resource(R.drawable.outline_exercise_24),
        backgroundColor = Color(0xFFE8F5E9),
        iconColor = Color(0xFF2E7D32)
    )
    object SUBSCRIPTION : Category(
        id = 9,
        displayNameRes = R.string.category_subscription,
        icon = CategoryIcon.Resource(R.drawable.outline_subscriptions_24),
        backgroundColor = Color(0xFFFFF8E1),
        iconColor = Color(0xFFF57F17)
    )
    object UTILITY : Category(
        id = 10,
        displayNameRes = R.string.category_utility,
        icon = CategoryIcon.Resource(R.drawable.outline_service_toolbox_24),
        backgroundColor = Color(0xFFE0F7FA),
        iconColor = Color(0xFF00838F)
    )
    object GENERAL : Category(
        id = 11,
        displayNameRes = R.string.category_general,
        icon = CategoryIcon.Resource(R.drawable.outline_apps_24),
        backgroundColor = Color(0xFFECEFF1),
        iconColor = Color(0xFF37474F)
    )
    object OTHERS : Category(
        id = 12,
        displayNameRes = R.string.category_others,
        icon = CategoryIcon.Resource(R.drawable.outline_stacks_24),
        backgroundColor = Color(0xFFF5F5F5),
        iconColor = Color(0xFF546E7A)
    )

    companion object {
        fun values() = listOf(
            FOOD, HEALTH, EDUCATION, ENTERTAINMENT, TRANSPORT, HOME, SHOPPING, GYM, SUBSCRIPTION, UTILITY, GENERAL, OTHERS
        )

        fun fromId(id: Int) = values().find { it.id == id } ?: OTHERS
        
        fun fromName(name: String) = values().find { it.name().equals(name, ignoreCase = true) } ?: OTHERS
    }

    fun name(): String = this::class.simpleName ?: "OTHERS"
}

fun getCategoryColors(category: Category): Pair<Color, Color> {
    return Pair(category.backgroundColor, category.iconColor)
}

sealed class CategoryIcon {
    data class Resource(@DrawableRes val resId: Int) : CategoryIcon()
}
