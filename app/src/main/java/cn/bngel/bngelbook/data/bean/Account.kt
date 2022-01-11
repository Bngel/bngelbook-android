package cn.bngel.bngelbook.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


/**
 * @author: bngel
 * @date: 21.11.19
 * @description:
 */

@Entity(tableName = "account")
data class Account(
    @PrimaryKey
    val id: Long?,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "user_id")
    val userId: Long?,
    @ColumnInfo(name = "balance")
    val balance: Double?
): Serializable, Bean()