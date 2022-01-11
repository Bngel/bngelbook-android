package cn.bngel.bngelbook.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "book")
data class Book(
    @PrimaryKey
    val id: Long?,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "user_id")
    val userId: Long?,
    @ColumnInfo(name = "type")
    val type: String?
): Serializable, Bean()
