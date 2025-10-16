package com.comunidadedevspace.imc.core.auth

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val isAuthenticated: Flow<Boolean>

    suspend fun refreshToken(): Result<Unit>

    fun logout()
}
