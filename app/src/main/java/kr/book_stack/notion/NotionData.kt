package kr.book_stack.notion

import kr.book_stack.api.ApiData

class NotionData {
    data class Book(
        val email: String,
        val page_id: String,
        val isbn: String,
        val book_status: String,
        val book_page: String,
        val look_page: String,
        val look_first: String,
        val look_last: String,
        val img: String,
    )

    data class User(
        val id: String,
        val name: String,
        val profileImg : String,
        val user_page_id: String,
        val book_page_id: String,
        val tag_page_id: String
    )
}