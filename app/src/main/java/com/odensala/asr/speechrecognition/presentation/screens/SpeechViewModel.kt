package com.odensala.asr.speechrecognition.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odensala.asr.keywords.domain.usecase.DetectKeywordsUseCase
import com.odensala.asr.speechrecognition.domain.model.AsrState
import com.odensala.asr.speechrecognition.domain.model.Word
import com.odensala.asr.speechrecognition.domain.repository.AsrRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SpeechViewModel @Inject constructor(
    private val asrRepository: AsrRepository,
    private val detectKeywordsUseCase: DetectKeywordsUseCase
) : ViewModel() {

    // Determines whether the flow is collected or not
    private val _isRecordingRequested = MutableStateFlow(false)

    // ASR flow that connects/disconnects based on recording state
    private val asrFlow = _isRecordingRequested
        .flatMapLatest { shouldRecord ->
            if (shouldRecord) {
                asrRepository.observeAsr()
                    .onEach { asrState ->
                        handleAsrState(asrState)
                    }
            } else {
                flowOf(AsrState.Disconnected)
                    .onEach { asrState -> handleAsrState(asrState) }
            }
        }

    private val _uiState = MutableStateFlow(SpeechUiState())
    val uiState: StateFlow<SpeechUiState> = combine(
        _uiState,
        asrFlow
    ) { uiState, _ ->
        uiState
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000),
        initialValue = SpeechUiState()
    )

    fun toggleRecording() {
        _isRecordingRequested.value = !_isRecordingRequested.value

        if (_isRecordingRequested.value) {
            _uiState.update { uiState ->
                uiState.copy(isLoading = true)
            }
        }
    }

    /**
     * Clear ASR error states.
     */
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

    private fun handleAsrState(asrState: AsrState) {
        _uiState.update { uiState ->
            when (asrState) {
                AsrState.Idle -> uiState.copy(
                    isRecording = false,
                    isLoading = false
                )

                AsrState.Connected -> uiState.copy(
                    isRecording = true,
                    isLoading = false
                )

                is AsrState.Transcribing -> {
                    val newText = asrState.words.joinToString("") { it.text }
                    Timber.d("Transcribing: text='$newText', isFinal=${asrState.isFinal}")

                    checkLatestWordForKeywords(asrState.words)

                    uiState.copy(
                        text = newText,
                        isRecording = true,
                        isLoading = false
                    )
                }

                is AsrState.Error -> {
                    _isRecordingRequested.value = false
                    // Stop recording when error occurs
                    uiState.copy(
                        error = asrState.error,
                        isRecording = false,
                        isLoading = false
                    )
                }

                AsrState.Disconnected -> uiState.copy(
                    isRecording = false,
                    isRecordingRequested = false,
                    isLoading = false
                )
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