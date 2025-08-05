package com.odensala.asr.speechrecognition.data.datasource.websocket

import com.odensala.asr.BuildConfig
import com.odensala.asr.speechrecognition.data.model.AsrResponseDto
import com.squareup.moshi.Moshi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AsrWebSocketDataSourceImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val moshi: Moshi
) : AsrWebSocketDataSource {

    private var webSocket: WebSocket? = null
    private val asrResponseDtoAdapter = moshi.adapter(AsrResponseDto::class.java)
    // private val accessToken =

    private var isConnected = false

    override fun startAsr(): Flow<WebSocketEvent> = callbackFlow {
        if (isConnected) {
            return@callbackFlow
        }

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Timber.d("WebSocket connected")
                isConnected = true
                trySend(WebSocketEvent.Connected)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val asrResponse = asrResponseDtoAdapter.fromJson(text)
                    asrResponse?.let {
                        trySend(WebSocketEvent.Message(it))
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Failed to parse ASR response: $text")
                    trySend(
                        WebSocketEvent.Error(
                            0,
                            "Parse error: ${e.message}",
                            e
                        )
                    )
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Timber.e(t, "WebSocket failure")
                isConnected = false
                trySend(
                    WebSocketEvent.Error(
                        0,
                        "Connection failed: ${t.message}",
                        t
                    )
                )
                close(t)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Timber.d("WebSocket closed: $code - $reason")
                isConnected = false
                trySend(WebSocketEvent.Disconnected(code, reason))
                close()
            }
        }

        val request = Request.Builder()
            .url(BuildConfig.MIMI_ASR_ENDPOINT)
            //.addHeader("Authorization", "Bearer $accessToken")
            .build()

        webSocket = okHttpClient.newWebSocket(request, listener)

        awaitClose {
            disconnect()
        }
    }

    override fun sendAudio(audioData: ByteArray) {
        webSocket?.send(ByteString.of(*audioData))
            ?: Timber.w("Cannot send audio: WebSocket not connected")
    }

    override fun disconnect() {
        webSocket?.close(1000, "Client disconnect")
        webSocket = null
    }
}