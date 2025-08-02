package com.odensala.asr.speechrecognition.data.repository

import com.odensala.asr.speechrecognition.data.datasource.AudioRecorderDataSource
import com.odensala.asr.speechrecognition.data.datasource.SpeechDataSource
import com.odensala.asr.speechrecognition.domain.model.Speech
import com.odensala.asr.speechrecognition.domain.repository.SpeechRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SpeechRepositoryImpl @Inject constructor(
    private val speechDataSource: SpeechDataSource,
    private val audioRecorderDataSource: AudioRecorderDataSource
) : SpeechRepository {

    override val isRecording: StateFlow<Boolean> = audioRecorderDataSource.isRecording

    override fun observeSpeechState(): Flow<Speech> =
        speechDataSource.observeSpeechState()

    override suspend fun startRecording() {

    }

    override suspend fun stopRecording() {

    }
}