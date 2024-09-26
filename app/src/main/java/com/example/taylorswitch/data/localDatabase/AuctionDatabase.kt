package com.example.taylorswitch.data.localDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [AuctionPostLocal::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AuctionDatabase : RoomDatabase() {

    abstract fun auctionDao(): AuctionDao

    companion object {
        @Volatile
        private var Instance: AuctionDatabase? = null

        fun getDatabase(context: Context): AuctionDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AuctionDatabase::class.java, "auction_database")
                    .build().also { Instance = it }
            }
        }
    }
}