package com.odensala.asr.speechrecognition.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * mimi ASR WebSocket response model
 */
@JsonClass(generateAdapter = true)
data class AsrResponseDto(
    val response: List<AsrResultDto>,
    @Json(name = "session_id") val sessionId: String,
    val status: String,
    val type: String
)