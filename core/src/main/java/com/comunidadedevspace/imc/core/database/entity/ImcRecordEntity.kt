package com.comunidadedevspace.imc.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "imc_records")
data class ImcRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val weightKg: Double,
    val heightMeters: Double,
    val bmi: Double,
    val classification: String,
    val recordedAt: Instant = Instant.now(),
    val synced: Boolean = false,
)
