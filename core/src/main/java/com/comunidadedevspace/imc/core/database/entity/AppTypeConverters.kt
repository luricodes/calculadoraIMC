package com.comunidadedevspace.imc.core.database.entity

import androidx.room.TypeConverter
import java.time.Instant

class AppTypeConverters {
    @TypeConverter
    fun fromInstant(instant: Instant?): Long? = instant?.toEpochMilli()

    @TypeConverter
    fun toInstant(value: Long?): Instant? = value?.let(Instant::ofEpochMilli)
}
