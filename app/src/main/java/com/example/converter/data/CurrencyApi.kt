package com.example.converter.data

import com.example.converter.BuildConfig.API_KEY
import com.example.converter.data.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {

    @GET("/latest")
    suspend fun getRates(
        @Query("base") base: String,
        @Query("access_key") access_key: String
    ): Response<CurrencyResponse>
}