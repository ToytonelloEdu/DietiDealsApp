package com.example.dietideals.data.persistence.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dietideals.data.persistence.entities.DbAuction
import kotlinx.coroutines.flow.Flow

@Dao
interface AuctionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(auction: DbAuction)

    @Update
    suspend fun update(auction: DbAuction)

    @Delete
    suspend fun delete(auction: DbAuction)

    @Query("SELECT * FROM auctions ORDER BY date DESC")
    suspend fun getAuctions(): List<DbAuction>

    @Query("SELECT * FROM auctions WHERE id = :id")
    suspend fun getAuctionById(id: Int): DbAuction

}