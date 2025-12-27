package com.example.proj.models

data class Coin(
    val id: Number,
    val name: String,
    val symbol: String,
    val slug: String,
    val quote: Map<String, Quote>
)
