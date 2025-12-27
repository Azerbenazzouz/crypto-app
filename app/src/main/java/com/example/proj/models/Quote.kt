package com.example.proj.models

data class Quote(
    val price: Double,
    val volumeChange24h: Double,
    val percentChange1h: Double,
    val percentChange24h: Double,
    val percentChange7d: Double,
    val lastUpdated: String
)
