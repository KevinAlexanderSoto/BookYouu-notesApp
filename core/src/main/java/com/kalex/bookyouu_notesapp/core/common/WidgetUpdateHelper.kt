package com.kalex.bookyouu_notesapp.core.common

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent

object WidgetUpdateHelper {
    fun updateObligationsWidget(context: Context) {
        val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        // We need to provide the component name or it won't be received by the specific receiver if it's not exported or if we want to be explicit.
        // But since we want to avoid depending on the :widget module here, we can use the action and let the system handle it, 
        // OR we can use the package name and class name as strings.
        
        val packageName = context.packageName
        val className = "$packageName.widget.ObligationsWidgetReceiver"
        
        intent.component = ComponentName(packageName, className)
        context.sendBroadcast(intent)
    }
}
