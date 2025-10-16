package com.comunidadedevspace.imc.feature.imc.domain

import javax.inject.Inject

class CalculateImcUseCase
    @Inject
    constructor() {
        operator fun invoke(
            weightKg: Double,
            heightMeters: Double,
        ): Pair<Double, ImcClassification> {
            val bmi = weightKg / (heightMeters * heightMeters)
            val classification = ImcClassification.fromBmi(bmi)
            return bmi to classification
        }
    }
