package com.odensala.asr.speechrecognition.data.repository

import com.odensala.asr.BuildConfig
import com.odensala.asr.speechrecognition.data.datasource.AudioRecorderDataSource
import com.odensala.asr.speechrecognition.data.datasource.auth.AuthDataSource
import com.odensala.asr.speechrecognition.data.datasource.websocket.AsrWebSocketDataSource
import com.odensala.asr.speechrecognition.data.mapper.toDomain
import com.odensala.asr.speechrecognition.domain.error.AsrError
import com.odensala.asr.speechrecognition.domain.model.AsrState
import com.odensala.asr.speechrecognition.domain.model.Token
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
    private val authDataSource: AuthDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AsrRepository {

    private suspend fun getAccessToken(): Result<Token> {
        Timber.d("Requesting access token")
        if (BuildConfig.MIMI_CLIENT_ID.isEmpty() || BuildConfig.MIMI_CLIENT_SECRET.isEmpty()) {
            val message =
                "No authentication credentials available"

            return Result.failure(IllegalStateException(message))
        }

        return authDataSource.requestAccessToken(
            clientId = BuildConfig.MIMI_CLIENT_ID,
            clientSecret = BuildConfig.MIMI_CLIENT_SECRET,
            scope = BuildConfig.MIMI_SCOPE
        )
    }

    /**
     * ASR の callbackFlow を作ります。
     * WebSocket と AudioRecorder はこの Flow に結びついていて、
     * Flow が collect ストップすると自動でキャンセルされます。
     *
     * 注意：Flow が collect を始める時に新しい token を作ります。
     * 本当は token をコントロールするクラスを用意して、
     * トークンを更新したり、もし local storage セーブするなら DataStore に
     * encrypt して安全にしまうのが理想です。
     */
    override fun observeAsr(): Flow<AsrState> = callbackFlow {
        Timber.d("AsrRepository: Flow collection started")

        // Get access token first
        val tokenResult = getAccessToken()

        val token = tokenResult.getOrElse { error ->
            Timber.e("Failed to obtain access token: ${error.message}")
            trySend(AsrState.Error(AsrError.AuthenticationFailed))
            close()
            return@callbackFlow
        }

        Timber.d("Successfully obtained access token")

        // Opens socket with token
        val webSocketJob = webSocket.startAsr(token.accessToken)
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
            Timber.d("AsrRepository: Cleanup completed")
        }
    }
}




