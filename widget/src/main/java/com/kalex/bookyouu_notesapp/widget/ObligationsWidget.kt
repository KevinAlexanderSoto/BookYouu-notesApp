package com.kalex.bookyouu_notesapp.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.layout.*
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.kalex.bookyouu_notesapp.payments.domain.model.Obligation
import com.kalex.bookyouu_notesapp.payments.domain.usecase.GetPendingObligationUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ObligationsWidget : GlanceAppWidget(), KoinComponent {

    override val stateDefinition = PreferencesGlanceStateDefinition
    private val getPendingObligationUseCase: GetPendingObligationUseCase by inject()

    companion object {
        val PAGE_KEY = intPreferencesKey("current_page")
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                val summary = getPendingObligationUseCase().collectAsState(listOf()).value
                val prefs = currentState<Preferences>()
                val currentPage = prefs[PAGE_KEY] ?: 0

                WidgetContent(summary = summary, currentPage = currentPage)
            }
        }
    }

    @Composable
    private fun WidgetContent(summary: List<Obligation>, currentPage: Int) {
        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
            maximumFractionDigits = 0
        }
        val normalizedPage = remember { mutableIntStateOf(0) }
        val totalPages = summary.size

        LaunchedEffect(currentPage, totalPages) {
            normalizedPage.intValue = if (totalPages > 0) {
                if (currentPage >= totalPages) 0 else if (currentPage < 0) totalPages - 1 else currentPage
            } else 0
        }

        Scaffold(
            backgroundColor = GlanceTheme.colors.surface,
            content = {
                if (summary.isEmpty()) {
                    Box(
                        modifier = GlanceModifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = LocalContext.current.getString(R.string.no_upcoming_payments),
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        )
                    }
                } else {
                    val obligation = summary[normalizedPage.intValue]

                    Column(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .padding(12.dp,16.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalAlignment = Alignment.Top
                    ) {
                        // Top Row: Icon, Name/Sub, Badge
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Icon in Circle
                            Box(
                                modifier = GlanceModifier
                                    .size(48.dp)
                                    .background(GlanceTheme.colors.secondaryContainer)
                                    .cornerRadius(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    provider = ImageProvider(getCategoryIcon(obligation.category)),
                                    contentDescription = null,
                                    modifier = GlanceModifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = GlanceModifier.width(12.dp))

                            Column(modifier = GlanceModifier.defaultWeight()) {
                                Text(
                                    text = obligation.name,
                                    maxLines = 1,
                                    style = TextStyle(
                                        color = GlanceTheme.colors.onSurface,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = obligation.category.lowercase()
                                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                                    style = TextStyle(
                                        color = GlanceTheme.colors.onSurfaceVariant,
                                        fontSize = 14.sp
                                    )
                                )
                            }

                            // Due Badge
                            val dueStatus = getDueStatus(obligation.dayOfMonth)
                            val badgeColor = if (dueStatus.isPastDue) GlanceTheme.colors.errorContainer else GlanceTheme.colors.tertiaryContainer
                            val onBadgeColor = if (dueStatus.isPastDue) GlanceTheme.colors.onErrorContainer else GlanceTheme.colors.onTertiaryContainer

                            Box(
                                modifier = GlanceModifier
                                    .background(badgeColor)
                                    .cornerRadius(16.dp)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = dueStatus.text,
                                    style = TextStyle(
                                        color = onBadgeColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = GlanceModifier.defaultWeight())

                        // Middle: Amount
                        Column(modifier = GlanceModifier.fillMaxWidth()) {
                            Text(
                                text = "TOTAL COMMITMENT",
                                style = TextStyle(
                                    color = GlanceTheme.colors.onSurfaceVariant,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                            Spacer(modifier = GlanceModifier.height(4.dp))
                            Text(
                                text = currencyFormatter.format(obligation.amount),
                                style = TextStyle(
                                    color = GlanceTheme.colors.primary,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = GlanceModifier.defaultWeight())

                        // Bottom: Arrows and Indicator
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                provider = ImageProvider(R.drawable.outline_arrow_back_24),
                                contentDescription = "Previous",
                                modifier = GlanceModifier
                                    .size(30.dp)
                                    .clickable(
                                        actionRunCallback<UpdatePageAction>(
                                            actionParametersOf(UpdatePageAction.KEY_INCREMENT to -1)
                                        )
                                    )
                            )

                            Spacer(modifier = GlanceModifier.width(16.dp))

                            Text(
                                text = "${normalizedPage.intValue + 1} / $totalPages",
                                style = TextStyle(
                                    color = GlanceTheme.colors.onSurfaceVariant,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )

                            Spacer(modifier = GlanceModifier.width(16.dp))

                            Image(
                                provider = ImageProvider(R.drawable.outline_arrow_forward_24),
                                contentDescription = "Next",
                                modifier = GlanceModifier
                                    .size(30.dp)
                                    .clickable(
                                        actionRunCallback<UpdatePageAction>(
                                            actionParametersOf(UpdatePageAction.KEY_INCREMENT to 1)
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        )
    }

    private fun getCategoryIcon(category: String): Int {
        return when (category.uppercase()) {
            "GYM" -> android.R.drawable.ic_lock_power_off
            "HOUSE" -> android.R.drawable.ic_menu_today
            "SUBSCRIPTION" -> android.R.drawable.ic_menu_slideshow
            "UTILITY" -> android.R.drawable.ic_menu_compass
            "TRANSPORT" -> android.R.drawable.ic_menu_directions
            else -> android.R.drawable.ic_menu_help
        }
    }

    private fun getDueStatus(dayOfMonth: Int): DueStatus {
        val today = Calendar.getInstance()
        val currentDay = today.get(Calendar.DAY_OF_MONTH)

        val targetDate = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

        val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
        val dateText = formatter.format(targetDate.time)

        return DueStatus(
            text = dateText,
            isPastDue = dayOfMonth < currentDay
        )
    }

    data class DueStatus(val text: String, val isPastDue: Boolean)
}

class UpdatePageAction : ActionCallback {
    companion object {
        val KEY_INCREMENT = ActionParameters.Key<Int>("increment")
    }

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val increment = parameters[KEY_INCREMENT] ?: 0
        updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
            val current = prefs[ObligationsWidget.PAGE_KEY] ?: 0
            prefs.toMutablePreferences().apply {
                this[ObligationsWidget.PAGE_KEY] = current + increment
            }
        }
        ObligationsWidget().update(context, glanceId)
    }
}
