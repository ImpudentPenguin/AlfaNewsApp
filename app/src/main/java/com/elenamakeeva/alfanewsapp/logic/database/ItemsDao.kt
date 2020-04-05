package com.elenamakeeva.alfanewsapp.logic.database

import androidx.room.*
import com.elenamakeeva.alfanewsapp.api.FavouriteNews
import com.elenamakeeva.alfanewsapp.api.Item
import com.elenamakeeva.alfanewsapp.api.News
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ItemsDao {

    @Query("SELECT * FROM items")
    fun getAllNews(): Flowable<List<Item>>

    @Query("SELECT * FROM favourite_news")
    fun getAllFavouriteNews(): Flowable<List<FavouriteNews>>

    @Query("SELECT * FROM favourite_news WHERE id == :itemsId")
    fun getFavouriteNews(itemsId: Int) : Flowable<FavouriteNews>

    @Insert
    fun insertItem(item: Item)

    @Delete
    fun deleteItem(item: Item)

    @Insert
    fun insertFavouriteItem(item: FavouriteNews)

    @Delete
    fun deleteFavouriteItem(item: FavouriteNews)
}