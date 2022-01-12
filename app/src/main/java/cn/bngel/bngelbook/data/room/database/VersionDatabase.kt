package cn.bngel.bngelbook.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import cn.bngel.bngelbook.data.bean.Version
import cn.bngel.bngelbook.data.room.dao.VersionDao


/**
 * @author: bngel
 * @date: 22.1.12
 * @description:
 */

@Database(entities = [Version::class], version = 1)
abstract class VersionDatabase: RoomDatabase() {

    abstract fun versionDao(): VersionDao
}