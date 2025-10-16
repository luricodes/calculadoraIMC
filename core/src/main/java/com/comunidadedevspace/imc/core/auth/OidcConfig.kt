package com.comunidadedevspace.imc.core.auth

data class OidcConfig(
    val authorizationEndpoint: String,
    val tokenEndpoint: String,
    val clientId: String,
    val redirectUri: String,
    val responseType: String = "code",
    val scopes: List<String> = listOf("openid", "profile", "email"),
)
