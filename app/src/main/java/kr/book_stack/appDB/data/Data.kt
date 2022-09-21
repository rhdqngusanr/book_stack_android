package kr.book_stack.appDB.data

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "book_table")
data class Book(
    @ColumnInfo(name = "email") val email: String?,
    @PrimaryKey val page_id: String,
    @ColumnInfo(name = "isbn") val isbn: String?,
    @ColumnInfo(name = "book_status") val bookStatus: String?,
    @ColumnInfo(name = "book_page") val bookPage: String?,
    @ColumnInfo(name = "look_page") val lookPage: String?,
    @ColumnInfo(name = "look_first") val lookFirst: String?,
    @ColumnInfo(name = "look_last") val lookLast: String?,
    @ColumnInfo(name = "img") val img: String?
    //@ColumnInfo(name = "tag") val tag: String?
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
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "tag")val tag: String?,
    @ColumnInfo(name = "tag_info") val tagInfo: String?,
    @ColumnInfo(name = "tag_img") val tagImg: String?,
)

data class DefaultTag(
    val name: String,
    val img: Int
)
