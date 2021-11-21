package cn.bngel.bngelbook.data.bookDao

import java.io.Serializable

data class Book(
    val id: Long?,
    val name: String?,
    val userId: Long?,
    val type: String?
): Serializable
