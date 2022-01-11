package cn.bngel.bngelbook.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    var id: Long? = null,
    @ColumnInfo(name = "birthday")
    var birthday: String? = null,
    @ColumnInfo(name = "email")
    var email: String? = null,
    @ColumnInfo(name = "gender")
    var gender: Int? = null,
    @ColumnInfo(name = "password")
    var password: String? = null,
    @ColumnInfo(name = "phone")
    var phone: String? = null,
    @ColumnInfo(name = "profile")
    var profile: String? = null,
    @ColumnInfo(name = "register_date")
    var registerDate: String? = null,
    @ColumnInfo(name = "username")
    var username: String? = null
): Serializable, Bean()