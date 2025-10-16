package com.comunidadedevspace.imc.feature.imc.presentation

import com.comunidadedevspace.imc.feature.imc.domain.ImcClassification
import com.comunidadedevspace.imc.feature.imc.domain.ImcRecord
import java.util.Locale

data class ImcUiState(
    val weightInput: String = "",
    val heightInput: String = "",
    val latestResult: String? = null,
    val classification: ImcClassification? = null,
    val history: List<ImcRecord> = emptyList(),
    val isOffline: Boolean = false,
    val errorMessage: String? = null,
    val locale: Locale = Locale.getDefault(),
)
