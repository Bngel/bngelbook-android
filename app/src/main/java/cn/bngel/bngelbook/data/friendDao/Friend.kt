package cn.bngel.bngelbook.data.friendDao

import java.io.Serializable

data class Friend(
    val id: Long,
    val user1Id: Long,
    val user2Id: Long
): Serializable
