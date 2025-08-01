package com.odensala.asr.speechrecognition.data.datasource

import com.odensala.asr.speechrecognition.domain.model.Speech
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SpeechDataSourceImpl @Inject constructor(): SpeechDataSource  {
    override fun observeRecordingState(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun observeSpeechState(): Flow<Speech> {
        TODO("Not yet implemented")
    }

    override suspend fun startAsr() {
        TODO("Not yet implemented")
    }

    override suspend fun stopAsr() {
        TODO("Not yet implemented")
    }
}