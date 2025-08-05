package com.odensala.asr.speechrecognition.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AsrResultDto(
    val pronunciation: String,
    val result: String,
    val time: List<Long>
)