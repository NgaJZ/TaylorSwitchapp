package com.example.taylorswitch.data.localDatabase


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AuctionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(auction: AuctionPostLocal)

    @Update
    suspend fun update(auction: AuctionPostLocal)

    @Delete
    suspend fun delete(auction: AuctionPostLocal)

    @Query("SELECT * from auctions WHERE id = :id")
    fun getAuction(id: Int): Flow<AuctionPostLocal>

    @Query("SELECT * from auctions ORDER BY name ASC")
    fun getAllAuctions(): Flow<List<AuctionPostLocal>>


}