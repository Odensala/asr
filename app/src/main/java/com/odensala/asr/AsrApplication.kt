package com.odensala.asr

import android.app.Application
import com.odensala.asr.core.utils.TimberLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AsrApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        TimberLogger.setupTimber()
    }
}