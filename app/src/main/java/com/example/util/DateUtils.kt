package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    fun getCurrentMonthRange(): Pair<Long, Long> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis

        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.MILLISECOND, -1)
        val end = cal.timeInMillis

        return Pair(start, end)
    }

    fun getCurrentMonthName(): String {
        val format = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        return format.format(Date())
    }

    fun formatExpenseDate(timestamp: Long): String {
        val target = Calendar.getInstance().apply { timeInMillis = timestamp }
        val now = Calendar.getInstance()

        return if (target.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
            target.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
        ) {
            "Today"
        } else if (target.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
            target.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) - 1
        ) {
            "Yesterday"
        } else {
            val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            format.format(Date(timestamp))
        }
    }

    fun formatNoteDateTime(timestamp: Long): String {
        val format = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        return format.format(Date(timestamp))
    }
}
