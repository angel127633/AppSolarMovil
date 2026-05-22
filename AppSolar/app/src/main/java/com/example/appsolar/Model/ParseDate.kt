package com.example.appsolar.Model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ParsedDate(
    val dayName: String,
    val dayNumber: String,
    val monthName: String
)

fun parseSolarDate(dateString: String): ParsedDate {

    val inputFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss",
        Locale.getDefault()
    )

    val date: Date = inputFormat.parse(dateString) ?: Date()

    val dayFormat = SimpleDateFormat(
        "EEE",
        Locale("es")
    )

    val dayNumberFormat = SimpleDateFormat(
        "dd",
        Locale("es")
    )

    val monthFormat = SimpleDateFormat(
        "MMM",
        Locale("es")
    )

    return ParsedDate(
        dayName = dayFormat.format(date)
            .replaceFirstChar { it.uppercase() },

        dayNumber = dayNumberFormat.format(date),

        monthName = monthFormat.format(date)
            .replaceFirstChar { it.uppercase() }
    )

}
