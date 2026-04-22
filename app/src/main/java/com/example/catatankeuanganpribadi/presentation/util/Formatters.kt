package com.example.catatankeuanganpribadi.presentation.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatters {
    private val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("in", "ID")).apply {
        maximumFractionDigits = 0
    }

    private val shortDateFormatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
    private val longDateTimeFormatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
    private val dayHeaderFormatter = SimpleDateFormat("EEEE, dd MMM", Locale("id", "ID"))

    fun rupiah(amount: Long): String = currencyFormatter.format(amount)

    fun shortDate(timestamp: Long): String = shortDateFormatter.format(Date(timestamp))

    fun longDateTime(timestamp: Long): String = longDateTimeFormatter.format(Date(timestamp))

    fun dayHeader(timestamp: Long): String = dayHeaderFormatter.format(Date(timestamp))
}
