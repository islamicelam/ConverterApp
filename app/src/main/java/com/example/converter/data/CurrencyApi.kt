package com.example.converter.data

import com.example.converter.data.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {

    @GET("/api/v2/latest")
    suspend fun getRates(
        @Query("base_currency") base: String,
        @Query("apikey") access_key: String
    ): Response<CurrencyResponse>
}