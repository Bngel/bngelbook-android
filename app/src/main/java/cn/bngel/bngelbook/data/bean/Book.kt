package cn.bngel.bngelbook.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Book(
    val id: Long?,
    val name: String?,
    val userId: Long?,
    val type: String?
): Serializable, Bean()
