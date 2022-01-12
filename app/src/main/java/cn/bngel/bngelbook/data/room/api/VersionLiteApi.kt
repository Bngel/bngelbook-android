package cn.bngel.bngelbook.data.room.api

import cn.bngel.bngelbook.data.bean.Version
import cn.bngel.bngelbook.data.room.DataManager

object VersionLiteApi {

    private val versionDB by lazy {
        DataManager.getVersionDB().versionDao()
    }

    fun insertVersion(version: Version) = versionDB.insertVersion(version)

    fun deleteVersion(version: Version) = versionDB.deleteVersion(version)

    fun getVersionById(id: Long) = versionDB.getVersionById(id)

    fun getAllVersions() = versionDB.getAllVersions()
}