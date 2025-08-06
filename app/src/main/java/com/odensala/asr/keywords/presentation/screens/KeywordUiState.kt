package com.odensala.asr.keywords.presentation.screens

import com.odensala.asr.keywords.domain.model.Keyword

data class KeywordUiState(
    val keywords: List<Keyword> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val newKeywordText: String = "",
    val isAddingKeyword: Boolean = false,
    val editingKeyword: Keyword? = null
)