package com.odensala.asr.speechrecognition.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.odensala.asr.R

@Composable
fun SpeechRecognitionScreen(
    viewModel: SpeechViewModel = hiltViewModel(),
    modifier: Modifier
) {

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

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
        isRecording = uiState.isRecording,
        text = uiState.text,
        onRecordingButtonClick = onButtonClick,
        modifier = modifier
    )
}

@Composable
fun SpeechRecognitionContent(
    isRecording: Boolean,
    text: String,
    onRecordingButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onRecordingButtonClick
        ) {
            Text(
                text = if (isRecording) stringResource(R.string.stop_recording) else stringResource(
                    R.string.start_recording
                ),
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = text,
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
        isRecording = false,
        text = "Sample text",
        onRecordingButtonClick = {}
    )
}