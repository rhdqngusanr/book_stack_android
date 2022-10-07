package kr.book_stack



import java.io.Serializable

class StructData {

    data class BookInfo(
        var inIsbn:  String,
        var inBookStatus:  String,
        var inBookPage:  String,
        var inLookPage:  String,
        var inLookFirst:  String,
        var inLookLast: String
        ):Serializable
}