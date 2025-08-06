package com.odensala.asr.keywords.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.odensala.asr.R
import com.odensala.asr.keywords.domain.model.Keyword

@Composable
fun KeywordItem(
    keyword: Keyword,
    onDelete: () -> Unit,
    onToggleStatus: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = keyword.keyword,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = if (keyword.isActive)
                        stringResource(R.string.active)
                    else
                        stringResource(R.string.inactive),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (keyword.isActive)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = keyword.isActive,
                    onCheckedChange = { onToggleStatus() }
                )

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(onClick = onEdit) {
                    Text(stringResource(R.string.edit))
                }

                TextButton(onClick = onDelete) {
                    Text(stringResource(R.string.delete))
                }
            }
        }
    }
}