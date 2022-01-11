package cn.bngel.bngelbook.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cn.bngel.bngelbook.data.bean.Book


/**
 * @author: bngel
 * @date: 22.1.11
 * @description:
 */

@Dao
interface BookDao {

    @Insert
    fun insertBook(book: Book): Int

    @Delete
    fun deleteBook(book: Book): Int

    @Query("SELECT * FROM book WHERE id = :id")
    fun getBookById(id: Long): Book

    @Query("SELECT * FROM book WHERE user_id = :userId")
    fun getBookByUserId(userId: Long): List<Book>
}