package cn.bngel.bngelbook.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "version")
data class Version(
    @PrimaryKey
    val id: Long?,
    @ColumnInfo(name = "version")
    val version: String?,
    @ColumnInfo(name = "content")
    val content: String?
): Serializable, Bean()
