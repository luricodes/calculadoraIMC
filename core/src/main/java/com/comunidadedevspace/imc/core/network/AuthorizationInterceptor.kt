package com.comunidadedevspace.imc.core.network

import com.comunidadedevspace.imc.core.auth.TokenStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor
    @Inject
    constructor(
        private val tokenStorage: TokenStorage,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val token = tokenStorage.getAccessToken()
            val request =
                if (!token.isNullOrBlank()) {
                    chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                } else {
                    chain.request()
                }
            return chain.proceed(request)
        }
    }
