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

data class Account(
    val id: Long?,
    val name: String?,
    val userId: Long?,
    val balance: Double?
): Serializable, Bean()