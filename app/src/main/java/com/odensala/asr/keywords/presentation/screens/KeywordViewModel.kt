package com.odensala.asr.keywords.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.odensala.asr.keywords.domain.model.Keyword
import com.odensala.asr.keywords.domain.repository.KeywordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class KeywordViewModel @Inject constructor(
    private val keywordRepository: KeywordRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(KeywordUiState())
    val uiState: StateFlow<KeywordUiState> = _uiState.asStateFlow()
    
    init {
        loadKeywords()
    }
    
    private fun loadKeywords() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            keywordRepository.getAllKeywords().collect { keywords ->
                Timber.d("Keywords loaded in ViewModel: ${keywords.size} items")
                _uiState.value = _uiState.value.copy(
                    keywords = keywords,
                    isLoading = false
                )
            }
        }
    }
    
    fun updateNewKeywordText(text: String) {
        _uiState.value = _uiState.value.copy(newKeywordText = text)
    }
    
    fun addKeyword() {
        val keywordText = _uiState.value.newKeywordText.trim()
        if (keywordText.isEmpty()) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAddingKeyword = true)
            
            val keyword = Keyword(keyword = keywordText)
            keywordRepository.insertKeyword(keyword)
            _uiState.value = _uiState.value.copy(
                newKeywordText = "",
                isAddingKeyword = false
            )
        }
    }
    
    fun deleteKeyword(keyword: Keyword) {
        viewModelScope.launch {
            keywordRepository.deleteKeyword(keyword)
        }
    }
    
    fun toggleKeywordActiveStatus(keyword: Keyword) {
        viewModelScope.launch {
            keywordRepository.updateKeywordActiveStatus(keyword.id, !keyword.isActive)
        }
    }
    
    fun startEditingKeyword(keyword: Keyword) {
        _uiState.value = _uiState.value.copy(
            editingKeyword = keyword,
            newKeywordText = keyword.keyword
        )
    }
    
    fun saveEditedKeyword() {
        val editingKeyword = _uiState.value.editingKeyword ?: return
        val newText = _uiState.value.newKeywordText.trim()
        if (newText.isEmpty()) return
        
        viewModelScope.launch {
            val updatedKeyword = editingKeyword.copy(keyword = newText)

            keywordRepository.updateKeyword(updatedKeyword)
            _uiState.value = _uiState.value.copy(
                editingKeyword = null,
                newKeywordText = ""
            )
        }
    }
    
    fun cancelEditing() {
        _uiState.value = _uiState.value.copy(
            editingKeyword = null,
            newKeywordText = ""
        )
    }
}