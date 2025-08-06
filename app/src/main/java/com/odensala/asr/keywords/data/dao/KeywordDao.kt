package com.odensala.asr.keywords.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.odensala.asr.keywords.data.entity.KeywordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KeywordDao {
    
    @Query("SELECT * FROM keywords ORDER BY createdAt DESC")
    fun getAllKeywords(): Flow<List<KeywordEntity>>
    
    @Query("SELECT * FROM keywords WHERE isActive = 1 ORDER BY keyword ASC")
    fun getActiveKeywords(): Flow<List<KeywordEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeyword(keyword: KeywordEntity): Long
    
    @Update
    suspend fun updateKeyword(keyword: KeywordEntity)
    
    @Delete
    suspend fun deleteKeyword(keyword: KeywordEntity)
    
    @Query("UPDATE keywords SET isActive = :isActive WHERE id = :id")
    suspend fun updateKeywordActiveStatus(id: Long, isActive: Boolean)
}