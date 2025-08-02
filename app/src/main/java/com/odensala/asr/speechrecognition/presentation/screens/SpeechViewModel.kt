package com.odensala.asr.speechrecognition.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odensala.asr.speechrecognition.domain.repository.SpeechRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpeechViewModel @Inject constructor(
    private val speechRepository: SpeechRepository
) : ViewModel() {
    val uiState: StateFlow<SpeechUiState> = combine(
        speechRepository.isRecording,
        speechRepository.observeSpeechState()
    ) { recordingState, speechState ->
        SpeechUiState(
            isRecording = recordingState,
            text = speechState.text
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SpeechUiState()
    )

    fun toggleRecording() {
        if (uiState.value.isRecording) {
            stopRecording()
        } else {
            startRecording()
        }
    }

    private fun startRecording() {
        viewModelScope.launch {
            speechRepository.startRecording()
        }
    }

    private fun stopRecording() {
        viewModelScope.launch {
            speechRepository.stopRecording()
        }
    }
}