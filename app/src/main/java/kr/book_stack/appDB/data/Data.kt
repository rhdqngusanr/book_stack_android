package kr.book_stack.appDB.data

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "book_table")
data class Book(
    @ColumnInfo(name = "email") val email: String?,
    @PrimaryKey val page_id: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "isbn") val isbn: String?,
    @ColumnInfo(name = "book_status") val bookStatus: String?,
    @ColumnInfo(name = "book_page") val bookPage: String?,
    @ColumnInfo(name = "look_page") val lookPage: String?,
    @ColumnInfo(name = "look_first") val lookFirst: String?,
    @ColumnInfo(name = "look_last") val lookLast: String?,
    @ColumnInfo(name = "img") val img: String?,
    @ColumnInfo(name = "tag") val tag: String?,
    @ColumnInfo(name = "tag_img") val tagImg: String?,
    @ColumnInfo(name = "highlight") val highlight: String?,
    @ColumnInfo(name = "comment") val comment: String?

)

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name")val name: String?,
    @ColumnInfo(name = "profile_img") val profileImg: String?,
    @ColumnInfo(name = "user_page_id") val userPageId: String?,
    @ColumnInfo(name = "book_page_id") val bookPageId: String?,
    @ColumnInfo(name = "tag_page_id") val tagPageId: String?,
)


@Entity(tableName = "tag_table")
data class Tag(
   // @PrimaryKey(autoGenerate = true) val id: Int = 0,
    //@ColumnInfo(name = "tag")val tag: String?,
    @PrimaryKey val tag: String,
    @ColumnInfo(name = "tag_img") val tagImg: String?,
)
@Entity(tableName = "result_tag_table")
data class ResultTag(
    // @PrimaryKey(autoGenerate = true) val id: Int = 0,
    //@ColumnInfo(name = "tag")val tag: String?,
    @PrimaryKey val tag: String,
    @ColumnInfo(name = "tag_img") val tagImg: String?,
    @ColumnInfo(name = "page_id") val pageId: String?,
)

data class DefaultTag(
    val name: String,
    val img: Int,
    var check : Boolean
)
