package com.odensala.asr.speechrecognition.domain.error

/**
 * Domain-level errors that can occur during ASR operations
 */
sealed class AsrError {
    object NetworkUnavailable : AsrError()
    data class ConnectionFailed(val reason: String) : AsrError()
    object AuthenticationFailed : AsrError()
    object ServerUnavailable : AsrError()
    object Timeout : AsrError()
    object MicrophonePermissionDenied : AsrError()
    data class Unknown(val message: String) : AsrError()
}