package com.odensala.asr.speechrecognition.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TokenResponseDto(
    @Json(name = "code")
    val code: Int,
    
    @Json(name = "error")
    val error: String,
    
    @Json(name = "status")
    val status: String,
    
    @Json(name = "progress")
    val progress: Int,
    
    @Json(name = "kind")
    val kind: String,
    
    @Json(name = "accessToken")
    val accessToken: String,
    
    @Json(name = "expires_in")
    val expiresIn: Int,
    
    @Json(name = "operationId")
    val operationId: String,
    
    @Json(name = "selfLink")
    val selfLink: String,
    
    @Json(name = "targetLink")
    val targetLink: String,
    
    @Json(name = "startTimestamp")
    val startTimestamp: Long,
    
    @Json(name = "endTimestamp")
    val endTimestamp: Long
)