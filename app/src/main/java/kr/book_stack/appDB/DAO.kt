package kr.book_stack.appDB

import androidx.lifecycle.LiveData
import androidx.room.*
import kr.book_stack.appDB.data.Book
import kr.book_stack.appDB.data.Tag
import kr.book_stack.appDB.data.User

@Dao
interface DAO {

    // 데이터 베이스 불러오기
    @Query("SELECT * from book_table")
    fun getAll(): LiveData<List<Book>>

    // 데이터 추가
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: Book)

    // 데이터 전체 삭제
    @Query("DELETE FROM book_table")
    fun deleteAll()

    // 데이터 업데이트
    @Update
    fun update(entity: Book);

    // 데이터 삭제
    @Delete
    fun delete(entity: Book);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(entity: User)


    @Query("SELECT * from user_table WHERE id = :inID")
    fun getUser(inID:String): LiveData<User>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTag(entity: Tag)

    @Query("SELECT * from tag_table")
    fun getTagAll(): LiveData<List<Tag>>
}
