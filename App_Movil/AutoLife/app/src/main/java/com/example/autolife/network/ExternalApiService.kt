package com.example.autolife.network

import com.example.autolife.model.EconomicIndicator
import retrofit2.http.GET

interface ExternalApiService {
    @GET("dolar")
    suspend fun getDolarValue(): EconomicIndicator
}