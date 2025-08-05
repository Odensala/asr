package com.odensala.asr.speechrecognition.data.datasource.websocket

import kotlinx.coroutines.flow.Flow

interface AsrWebSocketDataSource {
    fun startAsr(): Flow<WebSocketEvent>
    /**
     * Send audio PCM data to the WebSocket
     */
    fun sendAudio(audioData: ByteArray)
    fun disconnect()
}