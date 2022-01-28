package com.example.converter.main

import android.util.Log
import com.example.converter.data.CurrencyAPI
import com.example.converter.data.models.CurrencyResponse
import com.example.converter.util.Resource
import java.lang.Exception
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val api: CurrencyAPI
) {

    suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(base, "2bebd340-7d44-11ec-9fd4-6f3477693ca4")
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Log.d("TAG", "getRates: ")
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

}