package cn.bngel.bngelbook.network.api

import cn.bngel.bngelbook.dao.BasicDao
import cn.bngel.bngelbook.dao.BookDao
import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.bean.Book
import cn.bngel.bngelbook.utils.NetworkUtils

object BookApi: BaseApi() {

    private val bookService by lazy {
        BasicDao.create<BookDao>(BasicDao.BOOK_URL)
    }

    fun postBook(book: Book, event: ((CommonResult<Boolean>?) -> Unit)? = null){
        bookService.saveBook(book).enqueue(basicCallback(event))
    }

    fun deleteBookById(id: Long, event: ((CommonResult<Boolean>?) -> Unit)? = null){
        bookService.deleteBookById(id).enqueue(basicCallback(event))
    }

    fun updateBookById(book: Book, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        bookService.updateBookById(book).enqueue(basicCallback(event))
    }

    fun getBookById(id: Long, event: ((CommonResult<Book>?) -> Unit)? = null) {
        bookService.getBookById(id).enqueue(basicCallback(event))
    }

    fun getBooksByUserId(userId: Long, event: ((CommonResult<List<Book>>?) -> Unit)? = null) {
        bookService.getBooksByUserId(userId).enqueue(basicCallback(event))
    }
}