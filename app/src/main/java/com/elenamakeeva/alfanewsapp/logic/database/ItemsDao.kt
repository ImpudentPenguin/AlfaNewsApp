package com.elenamakeeva.alfanewsapp.logic.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.elenamakeeva.alfanewsapp.api.FavouriteNews
import com.elenamakeeva.alfanewsapp.api.Item

@Dao
interface ItemsDao {

    @Query("SELECT * FROM items")
    fun getAllNews(): LiveData<List<Item>>

    @Query("SELECT * FROM favourite_news")
    fun getAllFavouriteNews(): LiveData<List<FavouriteNews>>

    @Query("SELECT * FROM favourite_news WHERE id = :itemsId LIMIT 1")
    fun getFavouriteNews(itemsId: Int) : LiveData<FavouriteNews>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: Item)

    @Delete
    fun deleteItem(item: Item)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavouriteItem(item: FavouriteNews)

    @Delete
    fun deleteFavouriteItem(item: FavouriteNews)
}