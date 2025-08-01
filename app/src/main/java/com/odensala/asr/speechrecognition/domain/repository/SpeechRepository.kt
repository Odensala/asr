package com.odensala.asr.speechrecognition.domain.repository

import com.odensala.asr.speechrecognition.domain.model.Speech
import kotlinx.coroutines.flow.Flow

interface SpeechRepository {
    fun observeRecordingState(): Flow<Boolean>
    fun observeSpeechState(): Flow<Speech>
    suspend fun startRecording()
    suspend fun stopRecording()
}