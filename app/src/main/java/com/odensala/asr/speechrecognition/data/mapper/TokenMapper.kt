package com.odensala.asr.speechrecognition.data.mapper

import com.odensala.asr.speechrecognition.data.dto.TokenResponseDto
import com.odensala.asr.speechrecognition.domain.model.Token

fun TokenResponseDto.toDomain(): Token {
    return Token(
        accessToken = accessToken
    )
}