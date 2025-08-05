package com.odensala.asr.speechrecognition.data.repository

import com.odensala.asr.speechrecognition.data.datasource.AudioRecorderDataSource
import com.odensala.asr.speechrecognition.data.datasource.websocket.AsrWebSocketDataSource
import com.odensala.asr.speechrecognition.data.mapper.toDomain
import com.odensala.asr.speechrecognition.domain.model.AsrState
import com.odensala.asr.speechrecognition.domain.repository.AsrRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import timber.log.Timber
import javax.inject.Inject

class AsrRepositoryImpl @Inject constructor(
    private val webSocket: AsrWebSocketDataSource,
    private val audioRecorder: AudioRecorderDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AsrRepository {

    override fun observeAsr(): Flow<AsrState> = callbackFlow {
        // Opens socket
        val webSocketJob = webSocket.startAsr()
            .mapNotNull { event ->
                event.toDomain()
            }
            .onEach { domainModel ->
                trySend(domainModel)
            }
            .launchIn(this)

        // Starts microphone
        val audioRecorderJob = audioRecorder.startRecording()
            .onEach { audioChunk ->
                webSocket.sendAudio(audioChunk)
                Timber.v("Sent audio chunk: ${audioChunk.size} bytes")
            }
            .launchIn(this + ioDispatcher)

        awaitClose {
            webSocketJob.cancel()
            audioRecorderJob.cancel()
        }
    }
}




