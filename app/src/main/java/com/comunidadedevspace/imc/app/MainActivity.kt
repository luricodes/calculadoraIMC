package com.comunidadedevspace.imc.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.comunidadedevspace.imc.app.navigation.MainNavGraph
import com.comunidadedevspace.imc.app.notifications.NotificationHelper
import com.comunidadedevspace.imc.app.theme.ImcTheme
import com.comunidadedevspace.imc.app.update.InAppUpdateManager
import com.comunidadedevspace.imc.core.config.FeatureFlagKeys
import com.comunidadedevspace.imc.core.config.FeatureFlagManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var featureFlagManager: FeatureFlagManager

    @Inject
    lateinit var notificationHelper: NotificationHelper

    @Inject
    lateinit var inAppUpdateManager: InAppUpdateManager

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationHelper.createChannels()
        inAppUpdateManager.checkForUpdates(this)
        setContent {
            val coroutineScope = rememberCoroutineScope()
            val darkThemeState = userPreferences.isDarkTheme.collectAsState(initial = false)
            val localeState = userPreferences.selectedLocale.collectAsState(initial = Locale.ENGLISH)
            val toggleEnabled = remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                toggleEnabled.value = featureFlagManager.isEnabled(FeatureFlagKeys.DARK_MODE_TOGGLE)
            }
            ImcTheme(darkTheme = darkThemeState.value) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainApp(
                        darkTheme = darkThemeState.value,
                        darkModeToggleEnabled = toggleEnabled.value,
                        onToggleTheme = { enabled ->
                            coroutineScope.launch {
                                userPreferences.setDarkTheme(enabled)
                            }
                        },
                        currentLocale = localeState.value,
                        onLocaleChange = { locale ->
                            coroutineScope.launch {
                                userPreferences.setLocale(locale)
                            }
                        },
                    )
                }
            }
        }
    }
}

@Suppress("FunctionName")
@Composable
fun MainApp(
    darkTheme: Boolean,
    darkModeToggleEnabled: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    currentLocale: Locale,
    onLocaleChange: (Locale) -> Unit,
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MainNavGraph.START_DESTINATION) {
        MainNavGraph.build(
            builder = this,
            darkTheme = darkTheme,
            darkModeToggleEnabled = darkModeToggleEnabled,
            onToggleTheme = onToggleTheme,
            currentLocale = currentLocale,
            onLocaleChange = onLocaleChange,
        )
    }
}
