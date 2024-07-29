package com.example.dietideals.data.persistence.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dietideals.data.persistence.entities.DbOwnUser

@Dao
interface OwnUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: DbOwnUser)

    @Update
    suspend fun update(user: DbOwnUser)

    @Delete
    suspend fun delete(user: DbOwnUser)

    @Query("SELECT * FROM own_users")
    suspend fun getOwnUser(): DbOwnUser

}