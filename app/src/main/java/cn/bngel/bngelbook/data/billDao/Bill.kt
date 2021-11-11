package cn.bngel.bngelbook.data.billDao

import java.io.Serializable
import java.sql.Date

data class Bill(
    val id: Long,
    val type: String,
    val balance: Double,
    val accountId: Long? = null,
    val bookId: Long,
    val tags: String? = null,
    val createTime: Date,
    val io: Int
): Serializable