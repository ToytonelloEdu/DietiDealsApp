package com.example.dietideals.data.persistence.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.dietideals.data.persistence.entities.DbNotification
import com.example.dietideals.data.persistence.entities.DbNotificationWithAuction

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: DbNotification)

    @Delete
    suspend fun delete(notification: DbNotification)

    @Transaction
    @Query("SELECT * FROM notifications")
    suspend fun getNotifications(): List<DbNotificationWithAuction>

}