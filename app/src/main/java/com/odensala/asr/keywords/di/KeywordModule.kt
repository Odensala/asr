package com.odensala.asr.keywords.di

import android.content.Context
import androidx.room.Room
import com.odensala.asr.keywords.data.dao.KeywordDao
import com.odensala.asr.keywords.data.database.KeywordDatabase
import com.odensala.asr.keywords.data.repository.KeywordRepositoryImpl
import com.odensala.asr.keywords.domain.repository.KeywordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KeywordModule {

    @Provides
    @Singleton
    fun provideKeywordDatabase(@ApplicationContext context: Context): KeywordDatabase {
        return Room.databaseBuilder(
            context,
            KeywordDatabase::class.java,
            "keyword_database"
        ).build()
    }

    @Provides
    fun provideKeywordDao(database: KeywordDatabase): KeywordDao {
        return database.keywordDao()
    }

    @Provides
    @Singleton
    fun provideKeywordRepository(keywordDao: KeywordDao): KeywordRepository {
        return KeywordRepositoryImpl(keywordDao)
    }
}