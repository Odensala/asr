package com.odensala.asr.speechrecognition.data.mapper

import com.odensala.asr.speechrecognition.data.datasource.websocket.WebSocketEvent
import com.odensala.asr.speechrecognition.domain.enums.SpeechRecognitionStatus
import com.odensala.asr.speechrecognition.domain.model.AsrState
import com.odensala.asr.speechrecognition.domain.model.Word

internal fun WebSocketEvent.toDomain(): AsrState? = when (this) {
    is WebSocketEvent.Message -> {
        val words = asrResponseDto.response.map { dto ->
            Word(
                text = dto.result,
                startMs = dto.time[0],
                endMs = dto.time[1],
                kana = dto.pronunciation
            )
        }
        val final = asrResponseDto.status == SpeechRecognitionStatus.FINISHED.value
        AsrState.Transcribing(words, final)
    }

    is WebSocketEvent.Connected -> AsrState.Connected
    is WebSocketEvent.Disconnected -> AsrState.Disconnected
    is WebSocketEvent.Error -> AsrState.Error(toAsrError())
}