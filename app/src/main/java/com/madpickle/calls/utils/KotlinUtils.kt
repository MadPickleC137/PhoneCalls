package com.madpickle.calls.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.LinkedList
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


fun Long.convertEpochToFormattedTime(): String {
    // Создаем объект Date из epoch значения
    val date = Date(this)
    // Форматируем время "HH:MM"
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val timeString = timeFormat.format(date)
    // Возвращаем строку с временем
    return timeString
}

fun Long.convertEpochToFormattedDate(): String {
    // Создаем объект Date из epoch значения
    val date = Date(this)
    // Получаем текущий год
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    // Форматируем дату "{день месяца} {название месяца} {год}"
    val dateFormat = SimpleDateFormat("d MMMM", Locale.getDefault())
    var dateString = dateFormat.format(date)

    // Добавляем год, если он не текущий
    val year = SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
    if (year.toInt() != currentYear) {
        dateString += " $year"
    }

    // Возвращаем строку с временем и датой
    return dateString
}

fun Long.convertEpochToFormattedTimeDate(): String {
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


fun String.removePhoneTransformation(): String {
    if(this.contains(Regex("[*#]"))) {
        return this.dropLast(1)
    }
    var out = this
    if(this.length == 5) {
        out = out.replace(" ", "")
        out = out.dropLast(1)
        return out
    }
    if(this.length == 7) {
        out = this.substring(0, 5)
        return out
    }
    if(this.length == 11) {
        out = this.substring(0, 9)
        return out
    }
    if(this.length == 14) {
        out = this.substring(0, 12)
        return out
    }
    return out.dropLast(1)
}

fun String.addPhoneTransformation(): String {
    if(this.contains(Regex("[*#]"))) {
        return this
    }
    val chars = LinkedList<Char>()
    var out = ""
    this.forEach {
        chars.add(it)
    }
    if(this.length == 4) {
        chars.add(1, ' ')
    }
    if(this.length == 6) {
        chars.add(5, ' ')
    }
    if(this.length == 10) {
        chars.add(9, '-')
    }
    if(this.length == 13) {
        chars.add(12, '-')
    }
    if(this.length > 15) {
        return this.substring(0..14)
    }
    chars.forEach {
        out += it
    }
    return out
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

fun String.isValidPhone(): Boolean {
    return this.length > 1
//    // Регулярное выражение для проверки номера телефона по маске [x] [xxx] [xxx]-[xx]-[xx]
//    val regex = Regex("^\\d \\d{3} \\d{3}-\\d{2}-\\d{2}$")
//    return regex.matches(this)
}