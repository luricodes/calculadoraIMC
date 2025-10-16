package com.comunidadedevspace.imc.feature.imc.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.comunidadedevspace.imc.core.network.NetworkMonitor
import com.comunidadedevspace.imc.core.util.DispatcherProvider
import com.comunidadedevspace.imc.feature.imc.domain.CalculateImcUseCase
import com.comunidadedevspace.imc.feature.imc.domain.ObserveImcHistoryUseCase
import com.comunidadedevspace.imc.feature.imc.domain.PersistImcRecordUseCase
import com.comunidadedevspace.imc.feature.imc.domain.ScheduleSyncUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ImcViewModel
    @Inject
    constructor(
        observeHistory: ObserveImcHistoryUseCase,
        private val persistImcRecordUseCase: PersistImcRecordUseCase,
        private val scheduleSyncUseCase: ScheduleSyncUseCase,
        private val calculateImcUseCase: CalculateImcUseCase,
        private val dispatcherProvider: DispatcherProvider,
        private val networkMonitor: NetworkMonitor,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ImcUiState())
        val uiState: StateFlow<ImcUiState> = _uiState.asStateFlow()

        init {
            viewModelScope.launch {
                observeHistory().collectLatest { history ->
                    _uiState.value = _uiState.value.copy(history = history)
                }
            }

            viewModelScope.launch {
                networkMonitor.connectivity.collectLatest { connected ->
                    _uiState.value = _uiState.value.copy(isOffline = !connected)
                }
            }
        }

        fun onWeightChange(value: String) {
            _uiState.value = _uiState.value.copy(weightInput = value)
        }

        fun onHeightChange(value: String) {
            _uiState.value = _uiState.value.copy(heightInput = value)
        }

        fun onCalculate() {
            val weight = _uiState.value.weightInput.toDoubleOrNull()
            val height = _uiState.value.heightInput.toDoubleOrNull()
            if (weight == null || height == null || weight <= 0.0 || height <= 0.0) {
                _uiState.value = _uiState.value.copy(errorMessage = "invalid_input")
                return
            }
            val (bmi, classification) = calculateImcUseCase(weight, height)
            val locale = _uiState.value.locale
            _uiState.value =
                _uiState.value.copy(
                    latestResult = String.format(locale, "%.2f", bmi),
                    classification = classification,
                    errorMessage = null,
                )
            viewModelScope.launch(dispatcherProvider.io) {
                runCatching { persistImcRecordUseCase(weight, height) }
                    .onSuccess { record ->
                        scheduleSyncUseCase(record)
                    }
                    .onFailure { throwable ->
                        Timber.e(throwable, "Failed to persist IMC record")
                        _uiState.value = _uiState.value.copy(errorMessage = throwable.message)
                    }
            }
        }

        fun consumeError() {
            _uiState.value = _uiState.value.copy(errorMessage = null)
        }

        fun overrideLocale(locale: Locale) {
            _uiState.value = _uiState.value.copy(locale = locale)
        }
    }
