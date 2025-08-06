package com.odensala.asr.keywords.domain.usecase

import com.odensala.asr.keywords.domain.model.Keyword
import com.odensala.asr.keywords.domain.repository.KeywordRepository
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

/**
 * Gets keywords from the database and checks if the given text contains any of them.
 */
class DetectKeywordsUseCase @Inject constructor(
    private val keywordRepository: KeywordRepository
) {
    
    suspend operator fun invoke(text: String): List<Keyword> {
        return try {
            val activeKeywords = keywordRepository.getActiveKeywords().first()
            val detectedKeywords = activeKeywords.filter { keyword ->
                text.contains(keyword.keyword, ignoreCase = true)
            }
            
            Timber.d("DetectKeywordsUseCase: Found ${detectedKeywords.size} matching keywords")
            detectedKeywords
        } catch (e: Exception) {
            Timber.e("Error detecting keywords: ${e.message}")
            emptyList()
        }
    }
}