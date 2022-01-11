package cn.bngel.bngelbook.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.bngel.bngelbook.data.bean.Bill
import cn.bngel.bngelbook.data.room.dao.BillDao


/**
 * @author: bngel
 * @date: 22.1.11
 * @description:
 */

@Database(entities = [Bill::class], version = 1)
abstract class BillDatabase: RoomDatabase() {

    abstract fun billDao(): BillDao
}