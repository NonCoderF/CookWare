package com.sparkstudios.cookware.data.remote

import com.sparkstudios.cookware.data.remote.dto.CookRequest
import com.sparkstudios.cookware.data.remote.dto.CookResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface CookApi {
    @POST("functions/v1/cook")
    suspend fun analyze(@Body request: CookRequest): CookResponse
}
