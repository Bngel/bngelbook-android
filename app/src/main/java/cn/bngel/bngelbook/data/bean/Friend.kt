package cn.bngel.bngelbook.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "friend")
data class Friend(
    @PrimaryKey
    val id: Long?,
    @ColumnInfo(name = "user1_id")
    val user1Id: Long?,
    @ColumnInfo(name = "user2_id")
    val user2Id: Long?
): Serializable, Bean()
