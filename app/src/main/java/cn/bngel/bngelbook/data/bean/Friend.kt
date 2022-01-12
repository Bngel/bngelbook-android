package cn.bngel.bngelbook.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Friend(
    val id: Long?,
    val user1Id: Long?,
    val user2Id: Long?
): Serializable, Bean()
