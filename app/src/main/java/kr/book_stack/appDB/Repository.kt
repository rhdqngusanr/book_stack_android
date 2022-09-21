package kr.book_stack.appDB

import androidx.lifecycle.LiveData
import kr.book_stack.appDB.data.Book
import kr.book_stack.appDB.data.Tag
import kr.book_stack.appDB.data.User

class Repository(mDatabase: AppDatabase) {

    val dao = mDatabase.dao()
    val allBook: LiveData<List<Book>> = dao.getAll()
    val allTag: LiveData<List<Tag>> = dao.getTagAll()
    var getUserInfo: LiveData<User> = dao.getUser("")

    companion object {
        var sInstance: Repository? = null
        fun getInstance(database: AppDatabase): Repository {
            return sInstance
                ?: synchronized(this) {
                    val instance = Repository(database)
                    sInstance = instance
                    instance
                }
        }
    }

    suspend fun insert(entity: Book) {
        dao.insert(entity)
    }

    suspend fun delete(entity: Book) {
        dao.delete(entity)
    }

    suspend fun insertUser(entity: User) {
        dao.insertUser(entity)
    }
    suspend fun insertTag(entity: Tag) {
        dao.insertTag(entity)
    }
     fun getUser(inId:String): LiveData<User> {
        getUserInfo = dao.getUser(inId)
        return getUserInfo
    }
}