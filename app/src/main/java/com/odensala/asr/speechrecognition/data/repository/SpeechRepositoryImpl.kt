package com.odensala.asr.speechrecognition.data.repository

import com.odensala.asr.speechrecognition.data.datasource.SpeechDataSource
import com.odensala.asr.speechrecognition.domain.model.Speech
import com.odensala.asr.speechrecognition.domain.repository.SpeechRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SpeechRepositoryImpl @Inject constructor(private val speechDataSource: SpeechDataSource): SpeechRepository {
    override fun observeRecordingState(): Flow<Boolean> {
        return speechDataSource.observeRecordingState()
    }

    override fun observeSpeechState(): Flow<Speech> =
        speechDataSource.observeSpeechState()

    override suspend fun startRecording() {
        speechDataSource.startAsr()
    }

    override suspend fun stopRecording() {
        speechDataSource.stopAsr()
    }
}