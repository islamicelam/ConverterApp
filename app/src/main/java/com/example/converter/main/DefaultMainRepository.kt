package com.example.converter.main

import android.util.Log
import com.example.converter.data.CurrencyAPI
import com.example.converter.data.models.CurrencyResponse
import com.example.converter.util.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: CurrencyAPI
) : MainRepository {

    override suspend fun getRates(base: String): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates(base, "4ada570e341cb085c060e6b49d4a1edf")
            val result = response.body()
            if (response.isSuccessful && result != null) {
                //Log.d(TAG, "getRates: ")
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }

}