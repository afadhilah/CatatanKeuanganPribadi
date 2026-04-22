package com.example.catatankeuanganpribadi.presentation.util

import com.example.catatankeuanganpribadi.domain.model.DateRange
import com.example.catatankeuanganpribadi.presentation.model.PeriodFilter
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

object DateRangeFactory {

    fun create(
        periodFilter: PeriodFilter,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): DateRange {
        val now = Clock.System.now().toLocalDateTime(timeZone)
        val date = now.date

        val startDate = when (periodFilter) {
            PeriodFilter.DAY -> date
            PeriodFilter.WEEK -> date.minus(date.dayOfWeek.isoDayNumber - 1, DateTimeUnit.DAY)
            PeriodFilter.MONTH -> LocalDate(date.year, date.monthNumber, 1)
            PeriodFilter.YEAR -> LocalDate(date.year, 1, 1)
        }

        val endDateExclusive = when (periodFilter) {
            PeriodFilter.DAY -> startDate.plus(1, DateTimeUnit.DAY)
            PeriodFilter.WEEK -> startDate.plus(7, DateTimeUnit.DAY)
            PeriodFilter.MONTH -> startDate.plus(1, DateTimeUnit.MONTH)
            PeriodFilter.YEAR -> startDate.plus(1, DateTimeUnit.YEAR)
        }

        val start = startDate.atStartOfDayIn(timeZone).toEpochMilliseconds()
        val end = endDateExclusive.atStartOfDayIn(timeZone).toEpochMilliseconds() - 1

        return DateRange(start = start, end = end)
    }
}
