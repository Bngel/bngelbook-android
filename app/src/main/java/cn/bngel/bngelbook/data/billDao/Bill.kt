package cn.bngel.bngelbook.data.billDao

import java.io.Serializable
import java.sql.Date

data class Bill(
    val id: Long?,
    val accountId: Long?,
    val balance: Double,
    val bookId: Long,
    val createTime: Date?,
    val io: Int,
    val tags: String?,
    val type: String
): Serializable