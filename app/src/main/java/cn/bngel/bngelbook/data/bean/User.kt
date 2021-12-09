package cn.bngel.bngelbook.data.bean

import java.io.Serializable

data class User(
    val id: Long?,
    val birthday: String?,
    val email: String?,
    val gender: Int?,
    val password: String?,
    val phone: String?,
    val profile: String?,
    val registerDate: String?,
    val username: String?
): Serializable