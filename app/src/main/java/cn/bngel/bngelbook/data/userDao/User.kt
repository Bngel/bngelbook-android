package cn.bngel.bngelbook.data.userDao

import java.io.Serializable

data class User(
    val id: Int,
    val birthday: String?,
    val email: String?,
    val gender: Int?,
    val password: String,
    val phone: String,
    val profile: String?,
    val registerDate: String,
    val username: String
): Serializable