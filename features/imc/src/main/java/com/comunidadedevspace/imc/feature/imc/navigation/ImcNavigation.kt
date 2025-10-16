package com.comunidadedevspace.imc.feature.imc.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.comunidadedevspace.imc.feature.imc.presentation.ImcRoute
import java.util.Locale

const val IMC_ROUTE = "imc"

fun NavGraphBuilder.imcScreen(
    darkTheme: Boolean,
    darkModeToggleEnabled: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    currentLocale: Locale,
    onLocaleChange: (Locale) -> Unit,
) {
    composable(route = IMC_ROUTE) {
        ImcRoute(
            darkTheme = darkTheme,
            darkModeToggleEnabled = darkModeToggleEnabled,
            onToggleTheme = onToggleTheme,
            currentLocale = currentLocale,
            onLocalePersist = onLocaleChange,
        )
    }
}
