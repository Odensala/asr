package com.odensala.asr.keywords.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keywords")
data class KeywordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val keyword: String,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)