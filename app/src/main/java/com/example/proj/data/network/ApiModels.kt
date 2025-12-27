package com.example.proj.data.network

data class CryptoResponse(val data: List<CryptoDto>, val status: StatusDto)

data class StatusDto(
        val timestamp: String,
        val error_code: Int,
        val error_message: String?,
)

data class CryptoDto(
        val id: Int,
        val name: String,
        val symbol: String,
        val slug: String,
        val quote: Map<String, QuoteDto>
)

data class QuoteDto(val price: Double, val percent_change_24h: Double, val market_cap: Double)
