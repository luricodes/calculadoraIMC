package com.comunidadedevspace.imc.core.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl
    @Inject
    constructor(
        private val tokenStorage: TokenStorage,
    ) : AuthRepository {
        private val _isAuthenticated = MutableStateFlow(!tokenStorage.getAccessToken().isNullOrBlank())
        override val isAuthenticated: Flow<Boolean> = _isAuthenticated.asStateFlow()

        override suspend fun refreshToken(): Result<Unit> {
            // TODO: perform refresh using backend refresh endpoint via Retrofit.
            val refresh = tokenStorage.getRefreshToken()
            return if (refresh.isNullOrBlank()) {
                Result.failure(IllegalStateException("Missing refresh token"))
            } else {
                Result.success(Unit)
            }
        }

        override fun logout() {
            tokenStorage.clear()
            _isAuthenticated.value = false
        }
    }
