package com.kalex.bookyouu_notesapp.notification

import java.time.LocalDateTime

object AlarmUtils {
    fun calculateNextMonthlyAlarmTime(dayOfMonth: Int): LocalDateTime {
        val now = LocalDateTime.now()
        val currentMonth = now.month
        val currentYear = now.year

        // Handle months with fewer days than the requested dayOfMonth
        val maxDays = currentMonth.length(now.toLocalDate().isLeapYear)
        val targetDay = if (dayOfMonth > maxDays) maxDays else dayOfMonth

        var alarmTime = LocalDateTime.of(currentYear, currentMonth, targetDay, 9, 0)

        if (alarmTime.isBefore(now)) {
            // If it's already passed this month, schedule for next month
            val nextMonthDate = now.toLocalDate().plusMonths(1)
            val nextMonthMaxDays = nextMonthDate.month.length(nextMonthDate.isLeapYear)
            val nextTargetDay = if (dayOfMonth > nextMonthMaxDays) nextMonthMaxDays else dayOfMonth

            alarmTime = LocalDateTime.of(
                nextMonthDate.year,
                nextMonthDate.month,
                nextTargetDay,
                9, 0
            )
        }
        return alarmTime
    }
}