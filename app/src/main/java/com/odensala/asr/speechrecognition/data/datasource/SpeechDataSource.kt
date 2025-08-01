package com.odensala.asr.speechrecognition.data.datasource

import com.odensala.asr.speechrecognition.domain.model.Speech
import kotlinx.coroutines.flow.Flow

interface SpeechDataSource {
    fun observeRecordingState(): Flow<Boolean>
    fun observeSpeechState(): Flow<Speech>
    suspend fun startAsr()
    suspend fun stopAsr()
}