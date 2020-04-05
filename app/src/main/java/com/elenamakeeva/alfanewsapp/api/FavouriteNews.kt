package com.elenamakeeva.alfanewsapp.api

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_news")
data class FavouriteNews (
    override var title: String,
    override var link: String,
    override var pubDate: String
) : AbstractNews() {

    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0

    constructor(id: Int, title: String, link: String, pubDate: String) : this(title, link,  pubDate) {
        this.id = id
        this.title = title
        this.link = link
        this.pubDate = pubDate
    }
}
