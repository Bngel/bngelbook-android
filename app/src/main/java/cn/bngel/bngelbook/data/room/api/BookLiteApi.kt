package cn.bngel.bngelbook.data.room.api

import cn.bngel.bngelbook.data.bean.Book
import cn.bngel.bngelbook.data.room.DataManager


/**
 * @author: bngel
 * @date: 22.1.11
 * @description:
 */

object BookLiteApi {

    private val bookDB by lazy {
        DataManager.getBookDB().bookDao()
    }

    fun insertBook(book: Book) = bookDB.insertBook(book)

    fun deleteBook(book: Book) = bookDB.deleteBook(book)

    fun getBookById(id: Long) = bookDB.getBookById(id)

    fun getBookByUserId(userId: Long) = bookDB.getBookByUserId(userId)
}