package com.example.converter.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.converter.data.models.Data
import com.example.converter.util.DispatcherProvider
import com.example.converter.util.Resource
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val dispatchers: DispatcherProvider
): ViewModel() {

    sealed class CurrencyEvent {
        class Success(val resultText: String): CurrencyEvent()
        class Failure(val errorText: String): CurrencyEvent()
        object Loading : CurrencyEvent()
        object Empty : CurrencyEvent()
    }

    private val _conversion = MutableStateFlow<CurrencyEvent>(CurrencyEvent.Empty)
    val conversion: StateFlow<CurrencyEvent> = _conversion

    fun convert(
        amountStr: String,
        fromCurrency: String,
        toCurrency: String
    ) {
        val fromAmount = amountStr.toFloatOrNull()
        if(fromAmount == null) {
            _conversion.value = CurrencyEvent.Failure("Not a valid amount")
            return
        }
        Log.d("TAG", "to currency: $toCurrency")
        Log.d("TAG", "from currency: $fromCurrency")


        viewModelScope.launch(dispatchers.io) {
            _conversion.value = CurrencyEvent.Loading
            Log.d("TAG", "rates response: ${Gson().toJson(repository.getRates(fromCurrency))}")
            when(val ratesResponse = repository.getRates(fromCurrency)) {
                is Resource.Error -> _conversion.value = CurrencyEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.data
                    val rate = getRateForCurrency(toCurrency, rates)
                    if(rate == null) {
                        _conversion.value = CurrencyEvent.Failure("Unexpected error")
                    } else {
                        val convertedCurrency = round(fromAmount * rate * 100) / 100
                        _conversion.value = CurrencyEvent.Success(
                            "$fromAmount $fromCurrency = $convertedCurrency $toCurrency"
                        )
                    }
                }
            }
        }
    }

    private fun getRateForCurrency(currency: String, rates: Data) = when (currency) {
             "AED" -> rates.AED
             "AFN" -> rates.AFN
             "ALL" -> rates.ALL
             "AMD" -> rates.AMD
             "AOA" -> rates.AOA
             "ARS" -> rates.ARS
             "AUD" -> rates.AUD
             "AZN" -> rates.AZN
             "BDT" -> rates.BDT
             "BGN" -> rates.BGN
             "BHD" -> rates.BHD
             "BIF" -> rates.BIF
             "BIH" -> rates.BIH
             "BND" -> rates.BND
             "BOB" -> rates.BOB
             "BRL" -> rates.BRL
             "BSD" -> rates.BSD
             "BTC" -> rates.BTC
             "BWP" -> rates.BWP
             "BYR" -> rates.BYR
             "CAD" -> rates.CAD
             "CDF" -> rates.CDF
             "CHF" -> rates.CHF
             "CLP" -> rates.CLP
             "CNY" -> rates.CNY
             "COP" -> rates.COP
             "CRC" -> rates.CRC
             "CUC" -> rates.CUC
             "CVE" -> rates.CVE
             "CZK" -> rates.CZK
             "DJF" -> rates.DJF
             "DKK" -> rates.DKK
             "DOP" -> rates.DOP
             "DZD" -> rates.DZD
             "EGP" -> rates.EGP
             "ERN" -> rates.ERN
             "ETB" -> rates.ETB
             "ETH" -> rates.ETH
             "EUR" -> rates.EUR
             "FJD" -> rates.FJD
             "GBP" -> rates.GBP
             "GEL" -> rates.GEL
             "GHS" -> rates.GHS
             "GMD" -> rates.GMD
             "GNF" -> rates.GNF
             "GTQ" -> rates.GTQ
             "GYD" -> rates.GYD
             "HKD" -> rates.HKD
             "HNL" -> rates.HNL
             "HRV" -> rates.HRV
             "HTG" -> rates.HTG
             "HUF" -> rates.HUF
             "IDR" -> rates.IDR
             "ILS" -> rates.ILS
             "INR" -> rates.INR
             "IQD" -> rates.IQD
             "IRR" -> rates.IRR
             "ISK" -> rates.ISK
             "JMD" -> rates.JMD
             "JOD" -> rates.JOD
             "JPY" -> rates.JPY
             "KES" -> rates.KES
             "KGS" -> rates.KGS
             "KHR" -> rates.KHR
             "KMF" -> rates.KMF
             "KRW" -> rates.KRW
             "KYD" -> rates.KYD
             "KZT" -> rates.KZT
             "LAK" -> rates.LAK
             "LBP" -> rates.LBP
             "LKR" -> rates.LKR
             "LRD" -> rates.LRD
             "LSL" -> rates.LSL
             "LTC" -> rates.LTC
             "LYD" -> rates.LYD
             "MAD" -> rates.MAD
             "MDL" -> rates.MDL
             "MGA" -> rates.MGA
             "MKD" -> rates.MKD
             "MMK" -> rates.MMK
             "MNT" -> rates.MNT
             "MOP" -> rates.MOP
             "MUR" -> rates.MUR
             "MVR" -> rates.MVR
             "MWK" -> rates.MWK
             "MXN" -> rates.MXN
             "MYR" -> rates.MYR
             "MZN" -> rates.MZN
             "NAD" -> rates.NAD
             "NGN" -> rates.NGN
             "NIO" -> rates.NIO
             "NOK" -> rates.NOK
             "NPR" -> rates.NPR
             "NZD" -> rates.NZD
             "OMR" -> rates.OMR
             "PAB" -> rates.PAB
             "PEN" -> rates.PEN
             "PGK" -> rates.PGK
             "PHP" -> rates.PHP
             "PKR" -> rates.PKR
             "PLN" -> rates.PLN
             "PYG" -> rates.PYG
             "QAR" -> rates.QAR
             "RON" -> rates.RON
             "RSD" -> rates.RSD
             "RUB" -> rates.RUB
             "RWF" -> rates.RWF
             "SAR" -> rates.SAR
             "SCR" -> rates.SCR
             "SDG" -> rates.SDG
             "SEK" -> rates.SEK
             "SGD" -> rates.SGD
             "SLL" -> rates.SLL
             "SOS" -> rates.SOS
             else -> null
    }
}