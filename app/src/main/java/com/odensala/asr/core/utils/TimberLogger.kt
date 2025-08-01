package com.odensala.asr.core.utils

import com.odensala.asr.BuildConfig
import timber.log.Timber

object TimberLogger {
    fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return "(${element.fileName}: ${element.lineNumber})#${element.methodName}"
                }
            })
        }
    }
}