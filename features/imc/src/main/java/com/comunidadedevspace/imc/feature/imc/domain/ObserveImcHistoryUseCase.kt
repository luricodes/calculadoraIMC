package com.comunidadedevspace.imc.feature.imc.domain

import com.comunidadedevspace.imc.feature.imc.data.ImcRepository
import javax.inject.Inject

class ObserveImcHistoryUseCase
    @Inject
    constructor(
        private val repository: ImcRepository,
    ) {
        operator fun invoke() = repository.observeHistory()
    }
