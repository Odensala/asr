package com.odensala.asr.speechrecognition.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.odensala.asr.R
import com.odensala.asr.core.presentation.components.LoadingButton
import com.odensala.asr.keywords.presentation.components.KeywordDetectedDialog

@Composable
fun SpeechRecognitionScreen(
    modifier: Modifier = Modifier,
    onNavigateToKeywords: () -> Unit,
    viewModel: SpeechViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.toggleRecording()
        }
    }

    val onButtonClick = {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            viewModel.toggleRecording()
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }
    SpeechRecognitionContent(
        uiState = uiState,
        onRecordingButtonClick = onButtonClick,
        onNavigateToKeywords = onNavigateToKeywords,
        modifier = modifier
    )

    // Show keyword detection dialog
    if (uiState.showKeywordDialog) {
        KeywordDetectedDialog(
            detectedKeywords = uiState.detectedKeywords,
            onDismiss = viewModel::dismissKeywordDialog
        )
    }
}

@Composable
fun SpeechRecognitionContent(
    uiState: SpeechUiState,
    onRecordingButtonClick: () -> Unit,
    onNavigateToKeywords: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row {
            LoadingButton(
                onClick = onRecordingButtonClick,
                text = if (uiState.isRecording) stringResource(R.string.stop_recording) else stringResource(
                    R.string.start_recording
                ),
                isLoading = uiState.isLoading
            )

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedButton(
                onClick = onNavigateToKeywords
            ) {
                Text(stringResource(R.string.manage_keywords))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = uiState.text,
            onValueChange = { },
            label = { Text(stringResource(R.string.speech_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            readOnly = true,
            minLines = 8
        )
    }
}

@Preview
@Composable
fun SpeechRecognitionScreenPreview() {
    SpeechRecognitionContent(
        uiState = SpeechUiState(
            isRecording = false,
            text = "Sample text",
        ),
        onRecordingButtonClick = {},
        onNavigateToKeywords = {}
    )
}