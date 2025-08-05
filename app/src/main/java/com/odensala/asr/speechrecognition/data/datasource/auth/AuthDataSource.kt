package com.odensala.asr.speechrecognition.data.datasource.auth

import com.odensala.asr.speechrecognition.domain.model.Token

interface AuthDataSource {
    suspend fun requestAccessToken(
        clientId: String,
        clientSecret: String,
        scope: String
    ): Result<Token>
}