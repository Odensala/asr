package com.odensala.asr.speechrecognition.data.mapper

import com.odensala.asr.speechrecognition.data.datasource.websocket.WebSocketEvent
import com.odensala.asr.speechrecognition.domain.error.AsrError
import java.net.UnknownHostException

internal fun WebSocketEvent.Error.toAsrError(): AsrError = when {
    throwable is UnknownHostException -> AsrError.NetworkUnavailable
    code == 401 -> AsrError.AuthenticationFailed
    code == 400 -> AsrError.ServerUnavailable
    code == 408 -> AsrError.Timeout
    else -> AsrError.Unknown(message)
}