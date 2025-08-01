package com.odensala.asr.speechrecognition.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SpeechRecognitionScreen(
    viewModel: SpeechRecognitionViewModel = hiltViewModel(),
    modifier: Modifier
) {
    SpeechRecognitionContent()
}

@Composable
fun SpeechRecognitionContent() {

}

@Preview
@Composable
fun SpeechRecognitionScreenPreview() {
    SpeechRecognitionContent()
}