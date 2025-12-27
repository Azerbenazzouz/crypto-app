package com.example.proj.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoDao {
    @Query("SELECT * FROM favorites") fun getFavorites(): Flow<List<CryptoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(crypto: CryptoEntity)

    @Delete suspend fun delete(crypto: CryptoEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean
}
