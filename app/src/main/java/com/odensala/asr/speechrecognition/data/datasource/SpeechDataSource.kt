package com.odensala.asr.speechrecognition.data.datasource

import com.odensala.asr.speechrecognition.domain.model.Speech
import kotlinx.coroutines.flow.Flow

interface SpeechDataSource {
    fun observeSpeechState(): Flow<Speech>
    suspend fun startSpeechRecognition()
    suspend fun stopSpeechRecognition()
}