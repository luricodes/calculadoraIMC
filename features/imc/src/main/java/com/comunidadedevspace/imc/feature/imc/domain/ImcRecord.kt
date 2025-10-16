package com.comunidadedevspace.imc.feature.imc.domain

import java.time.Instant

data class ImcRecord(
    val id: Long,
    val weightKg: Double,
    val heightMeters: Double,
    val bmi: Double,
    val classification: ImcClassification,
    val recordedAt: Instant,
    val synced: Boolean,
)
