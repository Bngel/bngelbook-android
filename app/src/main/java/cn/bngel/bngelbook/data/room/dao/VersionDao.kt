package cn.bngel.bngelbook.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cn.bngel.bngelbook.data.bean.Version


/**
 * @author: bngel
 * @date: 22.1.12
 * @description:
 */

@Dao
interface VersionDao {

    @Insert
    fun insertVersion(version: Version): Int

    @Delete
    fun deleteVersion(version: Version): Int

    @Query("SELECT * FROM version WHERE id = :id")
    fun getVersionById(id: Long): Version

    @Query("SELECT * FROM version")
    fun getAllVersions(): List<Version>
}