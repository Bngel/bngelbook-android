package cn.bngel.bngelbook.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.sql.Date

@Entity(tableName = "bill")
data class Bill(
    @PrimaryKey
    val id: Long?,
    @ColumnInfo(name = "account_id")
    val accountId: Long?,
    @ColumnInfo(name = "balance")
    val balance: Double?,
    @ColumnInfo(name = "book_id")
    val bookId: Long?,
    @ColumnInfo(name = "create_time")
    val createTime: Date?,
    @ColumnInfo(name = "io")
    val io: Int?,
    @ColumnInfo(name = "tags")
    val tags: String?,
    @ColumnInfo(name = "type")
    val type: String?
): Serializable, Bean()