package com.example.converter.main

import com.example.converter.data.models.CurrencyResponse
import com.example.converter.util.Resource

interface MainRepository {

    suspend fun getRates(base: String): Resource<CurrencyResponse>
}