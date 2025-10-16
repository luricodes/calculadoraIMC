package com.comunidadedevspace.imc.feature.imc.domain

enum class ImcClassification(val min: Double, val max: Double, val labelRes: Int) {
    UNDERWEIGHT(Double.MIN_VALUE, 18.5, com.comunidadedevspace.imc.feature.imc.R.string.imc_underweight),
    NORMAL(18.5, 24.9, com.comunidadedevspace.imc.feature.imc.R.string.imc_normal),
    OVERWEIGHT(25.0, 29.9, com.comunidadedevspace.imc.feature.imc.R.string.imc_overweight),
    OBESE_I(30.0, 39.9, com.comunidadedevspace.imc.feature.imc.R.string.imc_obese_i),
    OBESE_II(40.0, Double.MAX_VALUE, com.comunidadedevspace.imc.feature.imc.R.string.imc_obese_ii),
    ;

    companion object {
        fun fromBmi(bmi: Double): ImcClassification = values().first { bmi >= it.min && bmi <= it.max }
    }
}
