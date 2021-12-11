package cn.bngel.bngelbook.data.bean

import java.io.Serializable

data class User(
    var id: Long? = null,
    var birthday: String? = null,
    var email: String? = null,
    var gender: Int? = null,
    var password: String? = null,
    var phone: String? = null,
    var profile: String? = null,
    var registerDate: String? = null,
    var username: String? = null
): Serializable