package com.elenamakeeva.alfanewsapp.api

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root
import org.simpleframework.xml.Text
import org.simpleframework.xml.Transient

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
    override var pubDate: String,

    @param:Element(name = "guid")
    @field:Element(name = "guid")
    override var guid: String

) : AbstractNews() {

    @PrimaryKey(autoGenerate = true)
    override var id: Int = 0

    constructor(id: Int,title: String, link: String, pubDate: String, guid: String) : this(title, link,  pubDate, guid) {
        this.id = id
        this.title = title
        this.link = link
        this.pubDate = pubDate
        this.guid = guid
    }
}
//} (
//
//    @param:Element(name = "title")
//    @field:Element(name = "title")
//    var title: String,
//
//    @param:Element(name = "link")
//    @field:Element(name = "link")
//    var link: String,
//
//    @param:Element(name = "pubDate")
//    @field:Element(name = "pubDate")
//    var pubDate: String,
//
//    @param:Element(name = "guid")
//    @field:Element(name = "guid")
//    var guid: String
//) {
//
//    @PrimaryKey(autoGenerate = true)
//    var id: Int = 0
//
//    constructor(id: Int,title: String, link: String, pubDate: String, guid: String) : this(title, link,  pubDate, guid) {
//        this.id = id
//        this.title = title
//        this.link = link
//        this.pubDate = pubDate
//        this.guid = guid
//    }
//}
//    class Description {
//
//        @Text(data = true)
//        var textNews: String? = null
//
//
//    }
//}