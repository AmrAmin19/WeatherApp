package com.example.weatherapp.model

import android.content.Context

fun String.toArabic(context: Context): String {


    // Check if the language is Arabic
    val locale = context.resources.configuration.locales[0]
    if (locale.language == "ar") {
        return this
            .replace("1", "١")
            .replace("2", "٢")
            .replace("3", "٣")
            .replace("4", "٤")
            .replace("5", "٥")
            .replace("6", "٦")
            .replace("7", "٧")
            .replace("8", "٨")
            .replace("9", "٩")
            .replace("0", "٠")
    } else {
        // Return the number as it is for other languages
        return this
    }
}