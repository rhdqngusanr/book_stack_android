package kr.book_stack



import kr.book_stack.appDB.data.DefaultTag
import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.NotionClient
import java.io.Serializable
import java.util.ArrayList

class StructData {
    companion object {
        val arrayTag = ArrayList<DefaultTag>()
    }
    data class BookInfo(
        var inIsbn:  String,
        var inBookStatus:  String,
        var inBookPage:  String,
        var inLookPage:  String,
        var inLookFirst:  String,
        var inLookLast: String
        ):Serializable
}