package cn.bngel.bngelbook.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.bngel.bngelbook.data.bean.Account
import cn.bngel.bngelbook.data.room.dao.AccountDao


/**
 * @author: bngel
 * @date: 22.1.11
 * @description:
 */

@Database(entities = [Account::class], version = 1)
abstract class AccountDatabase: RoomDatabase() {

    abstract fun accountDao(): AccountDao
}