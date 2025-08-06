package com.odensala.asr.keywords.data.repository

import com.odensala.asr.keywords.data.dao.KeywordDao
import com.odensala.asr.keywords.data.mapper.toDomain
import com.odensala.asr.keywords.data.mapper.toEntity
import com.odensala.asr.keywords.domain.model.Keyword
import com.odensala.asr.keywords.domain.repository.KeywordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeywordRepositoryImpl @Inject constructor(
    private val keywordDao: KeywordDao
) : KeywordRepository {

    override fun getAllKeywords(): Flow<List<Keyword>> {
        return keywordDao.getAllKeywords().map { entities ->
            val keywords = entities.map { it.toDomain() }
            Timber.d("Got ${keywords.size} keywords from database")
            keywords
        }
    }

    override fun getActiveKeywords(): Flow<List<Keyword>> {
        return keywordDao.getActiveKeywords().map { entities ->
            val keywords = entities.map { it.toDomain() }
            Timber.d("Got ${keywords.size} active keywords from database")
            keywords
        }
    }

    override suspend fun insertKeyword(keyword: Keyword): Long {
        val id = keywordDao.insertKeyword(keyword.toEntity())
        Timber.d("Inserted keyword successfully with id: $id")
        return id
    }

    override suspend fun updateKeyword(keyword: Keyword) {
        keywordDao.updateKeyword(keyword.toEntity())
        Timber.d("Updated keyword successuflly with id: ${keyword.id}, keyword: ${keyword.keyword}")
    }

    override suspend fun deleteKeyword(keyword: Keyword) {
        keywordDao.deleteKeyword(keyword.toEntity())
        Timber.d("Deleted keyword successfully with id: ${keyword.id}, keyword: ${keyword.keyword}")
    }

    override suspend fun updateKeywordActiveStatus(id: Long, isActive: Boolean) {
        keywordDao.updateKeywordActiveStatus(id, isActive)
        Timber.d("Updated keyword active status successfully with id: $id, isActive: $isActive")
    }
}