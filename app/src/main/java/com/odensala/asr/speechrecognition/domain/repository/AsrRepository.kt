package com.odensala.asr.speechrecognition.domain.repository

import com.odensala.asr.speechrecognition.domain.model.AsrState
import kotlinx.coroutines.flow.Flow

interface AsrRepository {
    fun observeAsr(): Flow<AsrState>
}
