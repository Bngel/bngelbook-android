package cn.bngel.bngelbook.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.bngel.bngelbook.data.bean.Friend
import cn.bngel.bngelbook.data.room.dao.FriendDao


/**
 * @author: bngel
 * @date: 22.1.11
 * @description:
 */

@Database(entities = [Friend::class], version = 1)
abstract class FriendDatabase: RoomDatabase() {

    abstract fun friendDao(): FriendDao
}