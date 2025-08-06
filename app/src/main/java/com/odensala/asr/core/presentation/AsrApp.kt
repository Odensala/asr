package com.odensala.asr.core.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.odensala.asr.core.presentation.navigation.AsrGraph
import com.odensala.asr.core.presentation.navigation.asrNavGraph

@Composable
fun AsrApp() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AsrGraph.SpeechScreen,
            modifier = Modifier.padding(innerPadding)
        ) {
            asrNavGraph(navController)
        }
    }
}