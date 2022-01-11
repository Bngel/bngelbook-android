package cn.bngel.bngelbook.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.bngel.bngelbook.data.bean.Book
import cn.bngel.bngelbook.data.room.dao.BookDao


/**
 * @author: bngel
 * @date: 22.1.11
 * @description:
 */

@Database(entities = [Book::class], version = 1)
abstract class BookDatabase: RoomDatabase() {

    abstract fun bookDao(): BookDao
}