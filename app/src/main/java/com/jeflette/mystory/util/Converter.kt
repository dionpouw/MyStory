package com.jeflette.mystory.util

import androidx.room.TypeConverter

class Converter {

    @TypeConverter
    fun fromAny(any: Any): Double {
        return any.toString().toDouble()
    }

    @TypeConverter
    fun toAny(double: Double): Any {
        return double
    }
}