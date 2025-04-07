package com.madpickle.calls.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun  Map<String, Boolean>.grantedAll (): Boolean {
    return this.all {
        it.value
    }
}

@SuppressLint("DefaultLocale")
fun Long.convertToMMSS(): String {
    if(this <= 0) return ""
    val minutes = this / 60
    val seconds = this % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)
    val unit = if (this < 60) "сек" else "мин"

    return "$formattedTime $unit"
}


fun Long.convertEpochToFormattedDate(): String {
    // Создаем объект Date из epoch значения
    val date = Date(this)

    // Получаем текущий год
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    // Форматируем время "HH:MM"
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = timeFormat.format(date)

    // Форматируем дату "{день месяца} {название месяца} {год}"
    val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
    var dateString = dateFormat.format(date)

    // Добавляем год, если он не текущий
    val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
    if (year.toInt() != currentYear) {
        dateString += " $year"
    }

    // Возвращаем строку с временем и датой
    return "$timeString\n$dateString"
}


fun String.formatPhoneNumber(): String {
    // Убираем все нецифровые символы из строки
    val digits = this.filter { it.isDigit() }

    // Проверяем, что количество цифр соответствует ожидаемому
    //иначе не применяем маску
    if (digits.length != 11) {
        return this
    }
    // Форматируем номер по маске +[x] [xxx] [xxx]-[xx]-[xx]
    return "+${digits[0]} ${digits.substring(1, 4)} ${digits.substring(4, 7)}-${digits.substring(7, 9)}-${digits.substring(9, 11)}"
}