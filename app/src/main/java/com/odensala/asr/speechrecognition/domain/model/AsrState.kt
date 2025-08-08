package com.odensala.asr.speechrecognition.domain.model

import com.odensala.asr.speechrecognition.domain.error.AsrError

/**
 * Domain model to represent ASR states.
 */
sealed class AsrState {
    object Idle : AsrState()
    object Connected : AsrState()
    data class Transcribing(val words: List<Word>, val isFinal: Boolean = false) : AsrState()
    data class Error(val error: AsrError) : AsrState()
    object Disconnected : AsrState()
}