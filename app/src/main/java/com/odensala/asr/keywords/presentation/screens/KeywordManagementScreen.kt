package com.odensala.asr.keywords.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.odensala.asr.R
import com.odensala.asr.keywords.domain.model.Keyword
import com.odensala.asr.keywords.presentation.components.KeywordItem

@Composable
fun KeywordManagementScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: KeywordViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    KeywordManagementContent(
        uiState = uiState,
        onNewKeywordTextChange = viewModel::updateNewKeywordText,
        onAddKeyword = viewModel::addKeyword,
        onDeleteKeyword = viewModel::deleteKeyword,
        onToggleKeywordStatus = viewModel::toggleKeywordActiveStatus,
        onStartEditing = viewModel::startEditingKeyword,
        onSaveEdit = viewModel::saveEditedKeyword,
        onCancelEdit = viewModel::cancelEditing,
        onNavigateBack = onNavigateBack,
        modifier = modifier
    )
}

@Composable
fun KeywordManagementContent(
    uiState: KeywordUiState,
    onNewKeywordTextChange: (String) -> Unit,
    onAddKeyword: () -> Unit,
    onDeleteKeyword: (Keyword) -> Unit,
    onToggleKeywordStatus: (Keyword) -> Unit,
    onStartEditing: (Keyword) -> Unit,
    onSaveEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
            Text(
                text = stringResource(R.string.keyword_management_title),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Add new keyword section
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = if (uiState.editingKeyword != null) 
                        stringResource(R.string.edit_keyword) 
                    else 
                        stringResource(R.string.add_new_keyword),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                OutlinedTextField(
                    value = uiState.newKeywordText,
                    onValueChange = onNewKeywordTextChange,
                    label = { Text(stringResource(R.string.keyword_label)) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row {
                    if (uiState.editingKeyword != null) {
                        Button(
                            onClick = onSaveEdit,
                            enabled = !uiState.isAddingKeyword
                        ) {
                            Text(stringResource(R.string.save))
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        TextButton(onClick = onCancelEdit) {
                            Text(stringResource(R.string.cancel))
                        }
                    } else {
                        Button(
                            onClick = onAddKeyword,
                            enabled = !uiState.isAddingKeyword && uiState.newKeywordText.isNotBlank()
                        ) {
                            if (uiState.isAddingKeyword) {
                                CircularProgressIndicator(modifier = Modifier.padding(4.dp))
                            } else {
                                Text(stringResource(R.string.add_keyword))
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Keywords list
        if (uiState.isLoading) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.loading_keywords))
            }
        } else {
            Text(
                text = stringResource(R.string.saved_keywords),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.keywords) { keyword ->
                    KeywordItem(
                        keyword = keyword,
                        onDelete = { onDeleteKeyword(keyword) },
                        onToggleStatus = { onToggleKeywordStatus(keyword) },
                        onEdit = { onStartEditing(keyword) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun KeywordManagementScreenPreview() {
    val sampleKeywords = listOf(
        Keyword(1, "こんにちは", true),
        Keyword(2, "ありがとう", false),
        Keyword(3, "さようなら", true)
    )
    
    KeywordManagementContent(
        uiState = KeywordUiState(
            keywords = sampleKeywords,
            newKeywordText = ""
        ),
        onNewKeywordTextChange = {},
        onAddKeyword = {},
        onDeleteKeyword = {},
        onToggleKeywordStatus = {},
        onStartEditing = {},
        onSaveEdit = {},
        onCancelEdit = {},
        onNavigateBack = {}
    )
}