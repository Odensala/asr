package com.odensala.asr.speechrecognition.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odensala.asr.speechrecognition.domain.model.AsrState
import com.odensala.asr.speechrecognition.domain.repository.AsrRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SpeechViewModel @Inject constructor(
    private val asrRepository: AsrRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SpeechUiState())
    val uiState: StateFlow<SpeechUiState> = _uiState.asStateFlow()

    private var asrJob: Job? = null

    fun toggleRecording() {
        if (asrJob?.isActive == true) stopRecording() else startRecording()
    }

    fun clearError() {
        _uiState.update { uiState ->
            uiState.copy(error = null)
        }
    }

    private fun cancelAsrJob() {
        asrJob?.cancel()
        asrJob = null
    }

    private fun startRecording() {
        if (asrJob?.isActive == true) return

        Timber.d("Starting ASR")
        asrJob = asrRepository.observeAsr()
            .onEach { asrState -> handleAsrState(asrState) }
            .launchIn(viewModelScope)
    }

    private fun stopRecording() {
        cancelAsrJob()
        _uiState.update { it.copy(isRecording = false, isConnected = false) }
        Timber.d("Stopping ASR")
    }

    private fun handleAsrState(asrState: AsrState) {
        _uiState.update { uiState ->
            when (asrState) {
                AsrState.Connected -> uiState.copy(isRecording = true, isConnected = true)
                is AsrState.Transcribing -> uiState.copy(
                    text = asrState.words.joinToString("") { it.text },
                    isConnected = true
                )

                is AsrState.Error -> {
                    cancelAsrJob()
                    uiState.copy(
                        error = asrState.error,
                        isRecording = false,
                        isConnected = false
                    )
                }

                AsrState.Disconnected -> uiState.copy(isRecording = false, isConnected = false)
            }
        }
    }
}