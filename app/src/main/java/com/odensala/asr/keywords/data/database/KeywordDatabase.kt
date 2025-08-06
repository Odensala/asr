package com.odensala.asr.keywords.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.odensala.asr.keywords.data.dao.KeywordDao
import com.odensala.asr.keywords.data.entity.KeywordEntity

@Database(
    entities = [KeywordEntity::class],
    version = 1
)
abstract class KeywordDatabase : RoomDatabase() {
    abstract fun keywordDao(): KeywordDao
}