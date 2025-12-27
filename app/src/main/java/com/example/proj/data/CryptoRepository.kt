package com.example.proj.data

import com.example.proj.data.local.CryptoDao
import com.example.proj.data.local.CryptoEntity
import com.example.proj.data.network.CoinMarketCapApi
import com.example.proj.data.network.CryptoDto
import kotlinx.coroutines.flow.Flow

class CryptoRepository(private val api: CoinMarketCapApi, private val dao: CryptoDao) {
    suspend fun getCoins(): Result<List<CryptoDto>> {
        return try {
            val response = api.getListings()
            if (response.status.error_code == 0) {
                Result.success(response.data)
            } else {
                Result.failure(Exception(response.status.error_message ?: "Unknown API Error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    val favorites: Flow<List<CryptoEntity>> = dao.getFavorites()

    suspend fun toggleFavorite(cryptoId: Int, name: String, symbol: String, price: Double) {
        val exists = dao.isFavorite(cryptoId)
        val entity = CryptoEntity(cryptoId, name, symbol, price)
        if (exists) {
            dao.delete(entity)
        } else {
            dao.insert(entity)
        }
    }
}
