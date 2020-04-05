package com.elenamakeeva.alfanewsapp.api

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Entity(tableName = "items")
@Root(name = "item", strict = false)
data class Item (
    @param:Element(name = "title")
    @field:Element(name = "title")
    override var title: String,

    @param:Element(name = "link")
    @field:Element(name = "link")
    override var link: String,

    @param:Element(name = "pubDate")
    @field:Element(name = "pubDate")
    override var pubDate: String
) : AbstractNews() {

    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0

    constructor(id: Int,title: String, link: String, pubDate: String) : this(title, link,  pubDate) {
        this.id = id
        this.title = title
        this.link = link
        this.pubDate = pubDate
    }
}