package kr.book_stack

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.book_stack.appDB.AppDatabase
import kr.book_stack.appDB.data.Book
import kr.book_stack.appDB.Repository
import kr.book_stack.appDB.data.ResultTag
import kr.book_stack.appDB.data.Tag
import kr.book_stack.appDB.data.User

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository =

    Repository(AppDatabase.getDatabase(application, viewModelScope))
    private var allBook: LiveData<List<Book>> = repository.allBook
    private var allTag: LiveData<List<Tag>> = repository.allTag
    private var allResultTag: LiveData<List<ResultTag>> = repository.allResultTag
    fun insert(entity: Book) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(entity)
    }


    fun deleteBook(entity: Book) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(entity)
    }

    fun getAll(): LiveData<List<Book>>{
        return allBook
    }

    fun insertUser(entity: User) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertUser(entity)
    }

    fun getUser(inId: String): LiveData<User> {
        return repository.getUser(inId)
    }

    fun insertTag(entity: Tag) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertTag(entity)
    }
    fun insertResultTag(entity: ResultTag) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertResultTag(entity)
    }
    fun getAllTag(): LiveData<List<Tag>>{
        return allTag
    }
    fun getAllResultTag(): LiveData<List<ResultTag>>{
        return allResultTag
    }
}