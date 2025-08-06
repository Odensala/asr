package com.odensala.asr.keywords.data.mapper

import com.odensala.asr.keywords.data.entity.KeywordEntity
import com.odensala.asr.keywords.domain.model.Keyword

fun KeywordEntity.toDomain(): Keyword {
    return Keyword(
        id = id,
        keyword = keyword,
        isActive = isActive,
        createdAt = createdAt
    )
}

fun Keyword.toEntity(): KeywordEntity {
    return KeywordEntity(
        id = id,
        keyword = keyword,
        isActive = isActive,
        createdAt = createdAt
    )
}