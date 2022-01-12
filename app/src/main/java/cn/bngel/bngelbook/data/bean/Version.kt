package cn.bngel.bngelbook.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Version(
    val id: Long?,
    val version: String?,
    val content: String?
): Serializable, Bean()
