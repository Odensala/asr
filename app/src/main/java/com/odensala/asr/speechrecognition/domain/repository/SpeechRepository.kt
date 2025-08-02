package com.odensala.asr.speechrecognition.domain.repository

import com.odensala.asr.speechrecognition.domain.model.Speech
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SpeechRepository {
    val isRecording: StateFlow<Boolean>
    fun observeSpeechState(): Flow<Speech>
    suspend fun startRecording()
    suspend fun stopRecording()
}