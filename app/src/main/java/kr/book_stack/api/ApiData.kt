package kr.book_stack.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.util.*


class ApiData{
    data class NotionCreateJson(
        @Expose
        @SerializedName("url")
        val url: String,
    )

    data class BookInfoAll(
        @Expose
        @SerializedName("items")
        val items: List<BookInfoItem>,
    )

    data class BookInfoItem(

        @Expose
        @SerializedName("title")
        val title: String,

        @Expose
        @SerializedName("image")
        val image: String

    )

    @Root(strict = false, name = "object")
    data class BookAlaDetailInfo(
        @field:ElementList(entry = "item", inline = true, required=false)
        @param:ElementList(entry = "item", inline = true, required=false)
        val ItemDetail:  List<ItemDetail>
    )
    @Root(strict = false, name = "item")
    data class ItemDetail(
        @field:ElementList(entry = "subInfo", inline = true, required=false)
        @param:ElementList(entry = "subInfo", inline = true, required=false)
        val BookInfo: List<ItemDetailPage>
    )
    @Root(strict = false, name = "subInfo")
    data class ItemDetailPage(
        @field:Element(name = "itemPage")
        @param:Element(name = "itemPage")
        val itemPage: Int
    )

    @Root(strict = false, name = "object")
    data class BookAlaInfo(
        @field:Element(name = "title")
        @param:Element(name = "title")
        val title: String,
        @field:ElementList(entry = "item", inline = true, required=false)
        @param:ElementList(entry = "item", inline = true, required=false)
        val item: List<Item?>
    )

    @Root(strict = false, name = "item")
    data class Item(
        @field:Element(name = "title")
        @param:Element(name = "title")
        var title: String,
        @field:Element(name = "cover")
        @param:Element(name = "cover")
        var cover: String,
        @field:Element(name = "isbn")
        @param:Element(name = "isbn")
        var isbn: String,
        @field:Element(name = "link")
        @param:Element(name = "link")
        var link: String,
        @field:Element(name = "description")
        @param:Element(name = "description")
        var description : String,
        @field:Element(name = "author")
        @param:Element(name = "author")
        var author: String? = "".toString() ,
        @field:Element(name = "publisher")
        @param:Element(name = "publisher")
        var publisher: String,
        @field:Element(name = "pubDate")
        @param:Element(name = "pubDate")
        var pubdate: String
    )



}