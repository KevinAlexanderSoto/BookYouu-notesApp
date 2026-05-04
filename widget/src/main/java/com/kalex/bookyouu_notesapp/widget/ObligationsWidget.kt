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

        LaunchedEffect(currentPage){
            normalizedPage.intValue = if (totalPages > 0) {
                if (currentPage >= totalPages) 0 else if (currentPage < 0) totalPages - 1 else currentPage
            } else 0
        }


        Scaffold(
            backgroundColor = GlanceTheme.colors.surface,
            titleBar = {
                Row(
                    modifier = GlanceModifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                   if (summary.isNotEmpty()) {
                        Image(
                            provider = ImageProvider(android.R.drawable.ic_media_previous),
                            contentDescription = "Previous",
                            modifier = GlanceModifier
                                .size(32.dp)
                                .clickable(actionRunCallback<UpdatePageAction>(
                                    actionParametersOf(UpdatePageAction.KEY_INCREMENT to -1)
                                ))
                        )
                        Spacer(modifier = GlanceModifier.width(24.dp))
                        Image(
                            provider = ImageProvider(android.R.drawable.ic_media_next),
                            contentDescription = "Next",
                            modifier = GlanceModifier
                                .size(32.dp)
                                .clickable(actionRunCallback<UpdatePageAction>(
                                    actionParametersOf(UpdatePageAction.KEY_INCREMENT to 1)
                                ))
                        )
                    }
                }
            },
            content = {
                if (summary.isEmpty()) {
                    Box(modifier = GlanceModifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = LocalContext.current.getString(R.string.no_upcoming_payments),
                            style = TextStyle(color = GlanceTheme.colors.onSurfaceVariant, fontSize = 12.sp)
                        )
                    }
                } else {
                    val obligation = summary[normalizedPage.intValue]

                    Column(
                        modifier = GlanceModifier.fillMaxSize().padding(bottom = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = GlanceModifier.defaultWeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            PaymentCard(obligation, currencyFormatter)
                        }
                        
                        // Page Indicator
                        Text(
                            text = "${normalizedPage.intValue + 1} / $totalPages",
                            style = TextStyle(
                                color = GlanceTheme.colors.onSurface,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        )
    }

    @Composable
    private fun PaymentCard(obligation: Obligation, currencyFormatter: NumberFormat) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.secondaryContainer)
                .cornerRadius(24.dp)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //Colocar la categoria
            //Colocar mas informacion de la fecha
            Text(
                text = "${obligation.dayOfMonth}",
                style = TextStyle(
                    color = GlanceTheme.colors.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = GlanceModifier.height(8.dp))
            Text(
                text = obligation.name,
                maxLines = 1,
                style = TextStyle(
                    color = GlanceTheme.colors.onSecondaryContainer,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            )
            Spacer(modifier = GlanceModifier.height(8.dp))
            Text(
                text = currencyFormatter.format(obligation.amount),
                style = TextStyle(
                    color = GlanceTheme.colors.onSecondaryContainer,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
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
