package com.odensala.asr.core.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.odensala.asr.speechrecognition.presentation.screens.SpeechRecognitionScreen

@Composable
fun AsrApp() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        SpeechRecognitionScreen(
            modifier = Modifier.padding(innerPadding)
        )
    }
}