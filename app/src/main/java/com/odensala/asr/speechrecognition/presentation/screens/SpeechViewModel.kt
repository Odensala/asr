package com.odensala.asr.speechrecognition.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odensala.asr.keywords.domain.usecase.DetectKeywordsUseCase
import com.odensala.asr.speechrecognition.domain.model.AsrState
import com.odensala.asr.speechrecognition.domain.model.Word
import com.odensala.asr.speechrecognition.domain.repository.AsrRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SpeechViewModel @Inject constructor(
    private val asrRepository: AsrRepository,
    private val detectKeywordsUseCase: DetectKeywordsUseCase
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
    
    fun dismissKeywordDialog() {
        _uiState.update { uiState ->
            uiState.copy(showKeywordDialog = false, detectedKeywords = emptyList())
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
                is AsrState.Transcribing -> {
                    val newText = asrState.words.joinToString("") { it.text }
                    Timber.d("Transcribing: text='$newText', isFinal=${asrState.isFinal}")
                    
                    checkLatestWordForKeywords(asrState.words)
                    
                    uiState.copy(
                        text = newText,
                        isConnected = true
                    )
                }

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
    
    private fun checkLatestWordForKeywords(words: List<Word>) {
        if (_uiState.value.showKeywordDialog) return

        // We take the last element of the list to ensure
        // we only display the dialog in the moment when the user says the keyword.
        val latestWord = words.lastOrNull()?.text ?: return
        if (latestWord.isEmpty()) return
        
        Timber.d("Latest word in the list: '$latestWord'")
        
        viewModelScope.launch {
            val detectedKeywords = detectKeywordsUseCase(latestWord)
            
            if (detectedKeywords.isNotEmpty()) {
                _uiState.update { uiState ->
                    uiState.copy(
                        detectedKeywords = detectedKeywords,
                        showKeywordDialog = true
                    )
                }
                Timber.d("Keywords detected: ${detectedKeywords.map { it.keyword }}")
            } else {
                Timber.d("No keywords detected in text: '$latestWord'")
            }
        }
    }
}