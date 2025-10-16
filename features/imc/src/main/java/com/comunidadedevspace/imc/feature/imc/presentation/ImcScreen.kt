package com.comunidadedevspace.imc.feature.imc.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.comunidadedevspace.imc.feature.imc.R
import com.comunidadedevspace.imc.feature.imc.domain.ImcClassification
import com.comunidadedevspace.imc.feature.imc.domain.ImcRecord
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Suppress("FunctionName")
@Composable
fun ImcRoute(
    darkTheme: Boolean,
    darkModeToggleEnabled: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    currentLocale: Locale,
    onLocalePersist: (Locale) -> Unit,
    viewModel: ImcViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(currentLocale) {
        viewModel.overrideLocale(currentLocale)
    }
    ImcScreen(
        state = uiState,
        onWeightChange = viewModel::onWeightChange,
        onHeightChange = viewModel::onHeightChange,
        onCalculate = viewModel::onCalculate,
        onErrorConsumed = viewModel::consumeError,
        onLocaleSelected = { locale ->
            viewModel.overrideLocale(locale)
            onLocalePersist(locale)
        },
        darkTheme = darkTheme,
        darkModeToggleEnabled = darkModeToggleEnabled,
        onToggleTheme = onToggleTheme,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("FunctionName")
@Composable
fun ImcScreen(
    state: ImcUiState,
    onWeightChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onCalculate: () -> Unit,
    onErrorConsumed: () -> Unit,
    onLocaleSelected: (Locale) -> Unit,
    darkTheme: Boolean,
    darkModeToggleEnabled: Boolean,
    onToggleTheme: (Boolean) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { messageKey ->
            val message =
                when (messageKey) {
                    "invalid_input" -> context.getString(R.string.error_invalid_input)
                    else -> messageKey
                }
            snackbarHostState.showSnackbar(message)
            onErrorConsumed()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    val toggleLabel = stringResource(id = R.string.action_toggle_dark_mode)
                    androidx.compose.material3.Switch(
                        checked = darkTheme,
                        onCheckedChange = { enabled -> onToggleTheme(enabled) },
                        enabled = darkModeToggleEnabled,
                        modifier = Modifier.semantics { contentDescription = toggleLabel },
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize(),
            contentPadding =
                PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = padding.calculateTopPadding() + 24.dp,
                    bottom = padding.calculateBottomPadding() + 24.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    LocaleChip(
                        locale = Locale.ENGLISH,
                        selected = state.locale.language == Locale.ENGLISH.language,
                        onLocaleSelected = onLocaleSelected,
                    )
                    LocaleChip(
                        locale = Locale.GERMAN,
                        selected = state.locale.language == Locale.GERMAN.language,
                        onLocaleSelected = onLocaleSelected,
                    )
                }
            }
            item {
                Column {
                    OutlinedTextField(
                        value = state.weightInput,
                        onValueChange = onWeightChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(id = R.string.label_weight)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = state.heightInput,
                        onValueChange = onHeightChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(id = R.string.label_height)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onCalculate, modifier = Modifier.fillMaxWidth()) {
                        Text(text = stringResource(id = R.string.action_calculate))
                    }
                    state.latestResult?.let { result ->
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = stringResource(id = R.string.label_result, result),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        state.classification?.let { classification ->
                            Text(
                                text = stringResource(id = classification.labelRes),
                                style = MaterialTheme.typography.titleSmall,
                            )
                        }
                    }
                }
            }

            if (state.history.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.title_history),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
                items(state.history) { record ->
                    HistoryRow(record = record, locale = state.locale)
                }
            }
        }
    }

    if (state.isOffline) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = stringResource(id = R.string.dialog_offline_title)) },
            text = { Text(text = stringResource(id = R.string.dialog_offline_message)) },
            confirmButton = {
                TextButton(onClick = { onLocaleSelected(state.locale) }) {
                    Text(text = stringResource(id = R.string.action_acknowledge))
                }
            },
        )
    }
}

@Suppress("FunctionName")
@Composable
private fun LocaleChip(
    locale: Locale,
    selected: Boolean,
    onLocaleSelected: (Locale) -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = { onLocaleSelected(locale) },
        label = { Text(text = locale.displayLanguage) },
    )
}

@Suppress("FunctionName")
@Composable
private fun HistoryRow(
    record: ImcRecord,
    locale: Locale,
) {
    val formatter =
        remember(locale) {
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", locale).withZone(ZoneId.systemDefault())
        }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = formatter.format(record.recordedAt))
        Text(text = String.format(locale, "%.2f", record.bmi))
        Text(text = record.classification.name)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
@Suppress("FunctionName")
@Composable
private fun ImcScreenPreview() {
    val state =
        ImcUiState(
            weightInput = "70",
            heightInput = "1.75",
            latestResult = "22.86",
            classification = ImcClassification.NORMAL,
            history =
                listOf(
                    ImcRecord(1, 70.0, 1.75, 22.86, ImcClassification.NORMAL, Instant.now(), true),
                ),
        )
    ImcScreen(
        state = state,
        onWeightChange = {},
        onHeightChange = {},
        onCalculate = {},
        onErrorConsumed = {},
        onLocaleSelected = {},
        darkTheme = false,
        darkModeToggleEnabled = true,
        onToggleTheme = {},
    )
}
