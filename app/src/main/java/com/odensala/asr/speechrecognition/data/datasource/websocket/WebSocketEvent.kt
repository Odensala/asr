package com.odensala.asr.speechrecognition.data.datasource.websocket

import com.odensala.asr.speechrecognition.data.model.AsrResponseDto

sealed class WebSocketEvent {
    data class Message(val asrResponseDto: AsrResponseDto) : WebSocketEvent()
    data class Error(
        val code: Int,
        val message: String,
        val throwable: Throwable
    ) : WebSocketEvent()
    data object Connected : WebSocketEvent()
    data class Disconnected(val code: Int, val reason: String) : WebSocketEvent()
}