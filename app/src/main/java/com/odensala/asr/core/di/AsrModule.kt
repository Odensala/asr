package com.odensala.asr.core.di

import android.content.Context
import com.odensala.asr.speechrecognition.data.datasource.AudioRecorderDataSource
import com.odensala.asr.speechrecognition.data.datasource.AudioRecorderDataSourceImpl
import com.odensala.asr.speechrecognition.data.datasource.websocket.AsrWebSocketDataSource
import com.odensala.asr.speechrecognition.data.datasource.websocket.AsrWebSocketDataSourceImpl
import com.odensala.asr.speechrecognition.data.repository.AsrRepositoryImpl
import com.odensala.asr.speechrecognition.domain.repository.AsrRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
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
    fun provideSpeechRepository(
        asrWebSocketDataSource: AsrWebSocketDataSource,
        audioRecorderDataSource: AudioRecorderDataSource
    ): AsrRepository {
        return AsrRepositoryImpl(asrWebSocketDataSource, audioRecorderDataSource)
    }

    @Provides
    @Singleton
    fun provideAsrWebSocketDataSource(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): AsrWebSocketDataSource {
        return AsrWebSocketDataSourceImpl(okHttpClient, moshi)
    }

    @Provides
    @Singleton
    fun provideAudioRecorderDataSource(@ApplicationContext context: Context): AudioRecorderDataSource {
        return AudioRecorderDataSourceImpl(context = context)
    }
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }
}