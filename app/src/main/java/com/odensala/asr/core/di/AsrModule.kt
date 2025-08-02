package com.odensala.asr.core.di

import android.content.Context
import com.odensala.asr.speechrecognition.data.datasource.AudioRecorderDataSource
import com.odensala.asr.speechrecognition.data.datasource.AudioRecorderDataSourceImpl
import com.odensala.asr.speechrecognition.data.datasource.SpeechDataSource
import com.odensala.asr.speechrecognition.data.datasource.SpeechDataSourceImpl
import com.odensala.asr.speechrecognition.data.repository.SpeechRepositoryImpl
import com.odensala.asr.speechrecognition.domain.repository.SpeechRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AsrModule {
    @Provides
    @Singleton
    fun provideSpeechRepository(speechDataSource: SpeechDataSource, audioRecorderDataSource: AudioRecorderDataSource): SpeechRepository {
        return SpeechRepositoryImpl(speechDataSource, audioRecorderDataSource)
    }

    @Provides
    @Singleton
    fun provideSpeechDataSource(): SpeechDataSource {
        return SpeechDataSourceImpl()
    }

    @Provides
    @Singleton
    fun provideAudioRecorderDataSource(@ApplicationContext context: Context): AudioRecorderDataSource {
        return AudioRecorderDataSourceImpl(context = context)
    }
}