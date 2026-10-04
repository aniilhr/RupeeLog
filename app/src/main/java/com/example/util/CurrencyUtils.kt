package com.example.util

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    private val wholeFormatter: DecimalFormat = DecimalFormat("#,##,##0").apply {
        maximumFractionDigits = 0
    }
    
    private val decimalFormatter: DecimalFormat = DecimalFormat("#,##,##0.00").apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    fun format(amount: Double?): String {
        val safeAmount = amount ?: 0.0
        return if (safeAmount % 1.0 == 0.0) {
            "₹" + wholeFormatter.format(safeAmount.toLong())
        } else {
            "₹" + decimalFormatter.format(safeAmount)
        }
    }
}
