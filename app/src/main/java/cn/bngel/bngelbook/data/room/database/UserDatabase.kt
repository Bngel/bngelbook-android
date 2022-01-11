package cn.bngel.bngelbook.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.bngel.bngelbook.data.bean.User
import cn.bngel.bngelbook.data.room.dao.UserDao


/**
 * @author: bngel
 * @date: 22.1.10
 * @description:
 */

@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

}