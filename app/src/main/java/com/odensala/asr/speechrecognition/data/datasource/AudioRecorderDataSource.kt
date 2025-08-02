package com.odensala.asr.speechrecognition.data.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Streams raw PCM audio as little-endian 16-bit mono frames.
 * The Flow completes when recording stops or an error occurs.
 */
interface AudioRecorderDataSource {
    val isRecording: StateFlow<Boolean>
    fun startRecording(): Flow<ByteArray>
    fun stopRecording()
}