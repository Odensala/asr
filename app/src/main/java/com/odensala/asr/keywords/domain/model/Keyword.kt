package com.odensala.asr.keywords.domain.model

data class Keyword(
    val id: Long = 0,
    val keyword: String,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)