package com.odensala.asr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.odensala.asr.core.presentation.AsrApp
import com.odensala.asr.core.presentation.theme.AsrTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("MainActivity: onCreate")
        enableEdgeToEdge()
        setContent {
            AsrTheme {
                AsrApp()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Timber.d("MainActivity: onStart")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("MainActivity: onResume")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("MainActivity: onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("MainActivity: onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("MainActivity: onDestroy")
    }
}