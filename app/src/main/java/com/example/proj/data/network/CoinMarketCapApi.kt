package com.example.proj.data.network

import retrofit2.http.GET
import retrofit2.http.Headers

interface CoinMarketCapApi {
    @Headers("X-CMC_PRO_API_KEY: 7b190dd3-91fa-4204-889e-f6b72e7f6b9a")
    @GET("v1/cryptocurrency/listings/latest")
    suspend fun getListings(): CryptoResponse
}
