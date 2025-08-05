package com.odensala.asr.speechrecognition.data.api

import com.odensala.asr.speechrecognition.data.dto.TokenResponseDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MimiAuthApi {
    
    @FormUrlEncoded
    @POST("v2/token")
    suspend fun requestToken(
        @Field("grant_type") grantType: String = "https://auth.mimi.fd.ai/grant_type/client_credentials",
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("scope") scope: String
    ): TokenResponseDto
}