package com.elenamakeeva.alfanewsapp.logic.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.elenamakeeva.alfanewsapp.api.FavouriteNews
import com.elenamakeeva.alfanewsapp.api.Item

@Database(entities = [Item::class, FavouriteNews::class], version = 13, exportSchema = false)
abstract class ItemsDatabase: RoomDatabase() {
    abstract fun itemsDao() : ItemsDao

    companion object{
        private const val DB_NAME: String = "news.db"
        private var database: ItemsDatabase? = null
        private var lock = Any()

        fun getInstance(context: Context): ItemsDatabase {
            if(database == null) {
                synchronized(lock) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        ItemsDatabase::class.java,
                        DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return database as ItemsDatabase

        }
    }
}