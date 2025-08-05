package com.odensala.asr.speechrecognition.data.datasource.auth

import com.odensala.asr.speechrecognition.data.api.MimiAuthApi
import com.odensala.asr.speechrecognition.data.mapper.toDomain
import com.odensala.asr.speechrecognition.domain.model.Token
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val authApi: MimiAuthApi
) : AuthDataSource {
    
    override suspend fun requestAccessToken(
        clientId: String,
        clientSecret: String,
        scope: String
    ): Result<Token> {
        return try {
            Timber.d("Requesting new access token from auth service")
            
            val response = authApi.requestToken(
                clientId = clientId,
                clientSecret = clientSecret,
                scope = scope
            )
            
            if (response.status == "success" && response.accessToken.isNotEmpty()) {
                Timber.d("Successfully obtained access token")
                Timber.d("Token expires in: ${response.expiresIn} seconds")
                Result.success(response.toDomain())
            } else {
                Timber.e("Token request failed: ${response.error}")
                Result.failure(Exception("Token request failed: ${response.error}"))
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Timber.e("HTTP ${e.code()}: $errorBody")
            Result.failure(Exception("HTTP ${e.code()}: $errorBody"))
        } catch (e: Exception) {
            Timber.e(e, "Failed to request access token")
            Result.failure(e)
        }
    }
}