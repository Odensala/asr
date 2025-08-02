package com.odensala.asr.speechrecognition.data.datasource

import com.odensala.asr.speechrecognition.domain.model.Speech
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class SpeechDataSourceImpl @Inject constructor(): SpeechDataSource  {
    override fun observeSpeechState(): Flow<Speech> {
        return flowOf(Speech("Hello World!"))
    }

    override suspend fun startSpeechRecognition() {

    }

    override suspend fun stopSpeechRecognition() {

    }
}