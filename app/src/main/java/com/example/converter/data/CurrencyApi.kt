package com.example.converter.data

import com.example.converter.data.models.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyAPI {

    @GET("/latest")
    suspend fun getRates(
        @Query("base") base: String,
        @Query("access_key") access_key: String = "4ada570e341cb085c060e6b49d4a1edf"
    ): Response<CurrencyResponse>
}