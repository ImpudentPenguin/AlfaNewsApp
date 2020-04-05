package com.elenamakeeva.alfanewsapp.api

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name="rss", strict = false)
data class News (

    @param:Element(name = "channel")
    @field:Element(name = "channel")
    var channel: Channel
) {
    @Root(strict = false)
    class Channel (

        @param:Element(name = "title")
        @field:Element(name = "title")
        var title: String,

        @param:ElementList(inline = true)
        @field:ElementList(inline = true)
        var itemList: List<Item>? = null
    )

}