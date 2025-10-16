package com.comunidadedevspace.imc.app.navigation

import androidx.navigation.NavGraphBuilder
import com.comunidadedevspace.imc.feature.imc.navigation.IMC_ROUTE
import com.comunidadedevspace.imc.feature.imc.navigation.imcScreen
import java.util.Locale

object MainNavGraph {
    const val START_DESTINATION: String = IMC_ROUTE

    fun build(
        builder: NavGraphBuilder,
        darkTheme: Boolean,
        darkModeToggleEnabled: Boolean,
        onToggleTheme: (Boolean) -> Unit,
        currentLocale: Locale,
        onLocaleChange: (Locale) -> Unit,
    ) {
        builder.imcScreen(
            darkTheme = darkTheme,
            darkModeToggleEnabled = darkModeToggleEnabled,
            onToggleTheme = onToggleTheme,
            currentLocale = currentLocale,
            onLocaleChange = onLocaleChange,
        )
    }
}
