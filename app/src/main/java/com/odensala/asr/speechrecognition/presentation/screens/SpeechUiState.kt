package com.odensala.asr.speechrecognition.presentation.screens

import com.odensala.asr.keywords.domain.model.Keyword
import com.odensala.asr.speechrecognition.domain.error.AsrError

data class SpeechUiState (
    val isLoading: Boolean = false,
    val isRecording: Boolean = false,
    val isConnected: Boolean = false,
    val text: String = "",
    val error: AsrError? = null,
    val detectedKeywords: List<Keyword> = emptyList(),
    val showKeywordDialog: Boolean = false
)