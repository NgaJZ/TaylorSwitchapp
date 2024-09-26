package com.example.taylorswitch.data.localDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TradePostLocal::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TradeDatabase : RoomDatabase() {
    abstract fun tradeDao(): TradeDao

    companion object {
        @Volatile
        private var Instance: TradeDatabase? = null

        fun  getDatabase(context: Context): TradeDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, TradeDatabase::class.java, "trade_database")
                    .build().also { Instance = it }
            }
        }
    }
}