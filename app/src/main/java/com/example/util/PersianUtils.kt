package com.example.util

import java.util.Calendar

private val persianDigits = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')

fun String.toPersianDigits(): String {
    val builder = StringBuilder(this.length)
    for (ch in this) {
        when (ch) {
            in '0'..'9' -> builder.append(persianDigits[ch - '0'])
            else -> builder.append(ch)
        }
    }
    return builder.toString()
}

fun Int.toPersianDigits(): String = this.toString().toPersianDigits()

fun Long.toPersianDigits(): String = this.toString().toPersianDigits()

fun formatPersianNumberWithCommas(value: String): String {
    val digitsOnly = value.filter { it.isDigit() }
    if (digitsOnly.isEmpty()) return value.toPersianDigits()
    val reversed = digitsOnly.reversed()
    val grouped = reversed.chunked(3).joinToString("٬").reversed()
    return grouped.toPersianDigits()
}

fun getTodayPersianDateString(): String {
    val cal = Calendar.getInstance()
    val gy = cal.get(Calendar.YEAR)
    val gm = cal.get(Calendar.MONTH) + 1
    val gd = cal.get(Calendar.DAY_OF_MONTH)
    val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

    val (jy, jm, jd) = gregorianToJalali(gy, gm, gd)
    val weekDayName = when (dayOfWeek) {
        Calendar.SATURDAY -> "شنبه"
        Calendar.SUNDAY -> "یکشنبه"
        Calendar.MONDAY -> "دوشنبه"
        Calendar.TUESDAY -> "سه‌شنبه"
        Calendar.WEDNESDAY -> "چهارشنبه"
        Calendar.THURSDAY -> "پنج‌شنبه"
        Calendar.FRIDAY -> "جمعه"
        else -> "امروز"
    }
    val monthName = when (jm) {
        1 -> "فروردین"
        2 -> "اردیبهشت"
        3 -> "خرداد"
        4 -> "تیر"
        5 -> "مرداد"
        6 -> "شهریور"
        7 -> "مهر"
        8 -> "آبان"
        9 -> "آذر"
        10 -> "دی"
        11 -> "بهمن"
        12 -> "اسفند"
        else -> ""
    }
    return "$weekDayName ${jd.toPersianDigits()} $monthName ${jy.toPersianDigits()}"
}

fun getCurrentPersianWeekdayIndex(): Int {
    val cal = Calendar.getInstance()
    return when (cal.get(Calendar.DAY_OF_WEEK)) {
        Calendar.SATURDAY -> 0
        Calendar.SUNDAY -> 1
        Calendar.MONDAY -> 2
        Calendar.TUESDAY -> 3
        Calendar.WEDNESDAY -> 4
        Calendar.THURSDAY -> 5
        Calendar.FRIDAY -> 6
        else -> 0
    }
}

val persianWeekdaysShort = listOf("ش", "ی", "د", "س", "چ", "پ", "ج")
val persianWeekdaysFull = listOf("شنبه", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنج‌شنبه", "جمعه")

private fun gregorianToJalali(gy: Int, gm: Int, gd: Int): Triple<Int, Int, Int> {
    val g_d_m = intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334)
    var gy2 = if (gm > 2) (gy + 1) else gy
    var days = 355666 + (365 * gy) + ((gy2 + 3) / 4) - ((gy2 + 99) / 100) + ((gy2 + 399) / 400) + gd + g_d_m[gm - 1]
    var jy = -1595 + (33 * (days / 12053))
    days %= 12053
    jy += 4 * (days / 1461)
    days %= 1461
    if (days > 365) {
        jy += (days - 1) / 365
        days = (days - 1) % 365
    }
    val jm: Int
    val jd: Int
    if (days < 186) {
        jm = 1 + (days / 31)
        jd = 1 + (days % 31)
    } else {
        jm = 7 + ((days - 186) / 30)
        jd = 1 + ((days - 186) % 30)
    }
    return Triple(jy, jm, jd)
}
