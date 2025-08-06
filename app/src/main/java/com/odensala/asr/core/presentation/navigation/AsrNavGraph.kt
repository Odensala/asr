package com.odensala.asr.core.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.odensala.asr.keywords.presentation.screens.KeywordManagementScreen
import com.odensala.asr.speechrecognition.presentation.screens.SpeechRecognitionScreen
import kotlinx.serialization.Serializable

fun NavGraphBuilder.asrNavGraph(navController: NavController) {
    composable<AsrGraph.SpeechScreen> {
        SpeechRecognitionScreen(
            onNavigateToKeywords = {
                navController.navigate(AsrGraph.KeywordScreen)
            }
        )
    }

    composable<AsrGraph.KeywordScreen> {
        KeywordManagementScreen(
            onNavigateBack = { navController.navigateUp() }
        )
    }
}

@Serializable
object AsrGraph {
    @Serializable
    object SpeechScreen

    @Serializable
    object KeywordScreen
}