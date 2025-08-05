package com.odensala.asr.speechrecognition.domain.model

data class Word(
    val text: String,
    val startMs: Long,
    val endMs: Long,
    val kana: String
)