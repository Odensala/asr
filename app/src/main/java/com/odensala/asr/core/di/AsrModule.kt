package com.odensala.asr.core.di

import com.odensala.asr.speechrecognition.data.datasource.SpeechDataSource
import com.odensala.asr.speechrecognition.data.datasource.SpeechDataSourceImpl
import com.odensala.asr.speechrecognition.data.repository.SpeechRepositoryImpl
import com.odensala.asr.speechrecognition.domain.repository.SpeechRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AsrModule {
    @Provides
    @Singleton
    fun provideSpeechRepository(speechDataSource: SpeechDataSource): SpeechRepository {
        return SpeechRepositoryImpl(speechDataSource)
    }

    @Provides
    @Singleton
    fun provideSpeechDataSource(): SpeechDataSource {
        return SpeechDataSourceImpl()
    }
}