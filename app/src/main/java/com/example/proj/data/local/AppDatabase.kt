package com.example.proj.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CryptoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cryptoDao(): CryptoDao
}
