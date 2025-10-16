package com.comunidadedevspace.imc.feature.imc.domain

import com.comunidadedevspace.imc.feature.imc.data.ImcRepository
import javax.inject.Inject

class PersistImcRecordUseCase
    @Inject
    constructor(
        private val repository: ImcRepository,
    ) {
        suspend operator fun invoke(
            weightKg: Double,
            heightMeters: Double,
        ) = repository.persist(weightKg, heightMeters)
    }
