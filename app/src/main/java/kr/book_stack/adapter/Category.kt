package kr.book_stack

import kr.book_stack.appDB.data.Book

class Category(val name: String, var item: List<Book>) {

    val listOfItems: List<Book> = item

}