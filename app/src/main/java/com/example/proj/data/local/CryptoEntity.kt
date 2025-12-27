package com.example.proj.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class CryptoEntity(
        @PrimaryKey val id: Int,
        val name: String,
        val symbol: String,
        val price: Double
)
