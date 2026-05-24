package com.kalex.bookyouu_notesapp.notification

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class AlarmUtilsTest {

    @Test
    fun `calculateNextMonthlyAlarmTime schedules for current month if day is in future`() {
        val now = LocalDateTime.now()
        val futureDay = if (now.dayOfMonth < 28) now.dayOfMonth + 1 else 1
        
        val result = AlarmUtils.calculateNextMonthlyAlarmTime(futureDay)
        
        if (futureDay > now.dayOfMonth) {
            assertEquals(now.month, result.month)
            assertEquals(now.year, result.year)
        } else {
            // If we rolled over to 1, it might be next month
            assertEquals(now.plusMonths(1).month, result.month)
        }
        assertEquals(futureDay, result.dayOfMonth)
        assertEquals(9, result.hour)
    }

    @Test
    fun `calculateNextMonthlyAlarmTime schedules for next month if day is in past`() {
        val now = LocalDateTime.now()
        if (now.dayOfMonth > 1) {
            val pastDay = now.dayOfMonth - 1
            val result = AlarmUtils.calculateNextMonthlyAlarmTime(pastDay)
            
            assertEquals(now.plusMonths(1).month, result.month)
            assertEquals(pastDay, result.dayOfMonth)
        }
    }

    @Test
    fun `calculateNextMonthlyAlarmTime handles last day of month correctly`() {
        // Test with a large day like 31
        val result = AlarmUtils.calculateNextMonthlyAlarmTime(31)
        
        // The result should either be the 31st (if it exists) or the last day of that month
        val maxDays = result.month.length(result.toLocalDate().isLeapYear)
        assertTrue(result.dayOfMonth <= 31)
        if (maxDays < 31) {
            assertEquals(maxDays, result.dayOfMonth)
        }
    }
}