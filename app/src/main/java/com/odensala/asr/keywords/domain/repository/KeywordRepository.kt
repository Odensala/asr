package com.odensala.asr.keywords.domain.repository

import com.odensala.asr.keywords.domain.model.Keyword
import kotlinx.coroutines.flow.Flow

interface KeywordRepository {
    fun getAllKeywords(): Flow<List<Keyword>>
    fun getActiveKeywords(): Flow<List<Keyword>>
    suspend fun insertKeyword(keyword: Keyword)
    suspend fun updateKeyword(keyword: Keyword)
    suspend fun deleteKeyword(keyword: Keyword)
    suspend fun updateKeywordActiveStatus(id: Long, isActive: Boolean)
}