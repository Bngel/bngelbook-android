package cn.bngel.bngelbook.data.bean

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
): Serializable