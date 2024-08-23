package com.example.dietideals.data.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dietideals.data.persistence.daos.AuctionDao
import com.example.dietideals.data.persistence.daos.OwnUserDao
import com.example.dietideals.data.persistence.entities.DbAuction
import com.example.dietideals.data.persistence.entities.DbLastBid
import com.example.dietideals.data.persistence.entities.DbOwnUser

@Database(
    entities = [DbAuction::class, DbOwnUser::class, DbLastBid::class],
    version = 4,
    exportSchema = false,
)
abstract class AppDatabase: RoomDatabase(){

    abstract fun auctionDao(): AuctionDao
    abstract fun ownUserDao(): OwnUserDao


    companion object{
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }

}