package com.example.proj.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://pro-api.coinmarketcap.com/"

    val api: CoinMarketCapApi by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CoinMarketCapApi::class.java)
    }
}
