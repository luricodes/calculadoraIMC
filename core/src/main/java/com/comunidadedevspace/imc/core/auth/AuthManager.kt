package com.comunidadedevspace.imc.core.auth

import android.app.Application
import android.net.Uri
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.TokenRequest
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager
    @Inject
    constructor(
        private val application: Application,
        private val tokenStorage: TokenStorage,
    ) {
        private val authService by lazy { AuthorizationService(application) }

        fun buildAuthRequest(config: OidcConfig): AuthorizationRequest {
            val serviceConfig =
                AuthorizationServiceConfiguration(
                    Uri.parse(config.authorizationEndpoint),
                    Uri.parse(config.tokenEndpoint),
                )
            return AuthorizationRequest.Builder(
                serviceConfig,
                config.clientId,
                config.responseType,
                Uri.parse(config.redirectUri),
            )
                .setScopes(config.scopes)
                .build()
        }

        fun exchangeToken(
            tokenRequest: TokenRequest,
            clientAuthentication: ClientAuthentication,
            onResult: (Result<Unit>) -> Unit,
        ) {
            authService.performTokenRequest(tokenRequest, clientAuthentication) { response, ex ->
                if (response != null) {
                    tokenStorage.saveTokens(
                        response.accessToken.orEmpty(),
                        response.refreshToken.orEmpty(),
                    )
                    onResult(Result.success(Unit))
                } else {
                    Timber.e(ex, "Token exchange failed")
                    onResult(Result.failure(ex ?: IllegalStateException("Token response null")))
                }
            }
        }

        fun clearSession() {
            tokenStorage.clear()
        }
    }
