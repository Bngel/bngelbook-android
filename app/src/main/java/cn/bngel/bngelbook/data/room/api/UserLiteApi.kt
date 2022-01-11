package cn.bngel.bngelbook.data.room.api

import cn.bngel.bngelbook.data.bean.User
import cn.bngel.bngelbook.data.room.DataManager

object UserLiteApi {

    private val userDB by lazy {
        DataManager.getUserDB().userDao()
    }

    fun insertUser(user: User) = userDB.insertUser(user)

    fun deleteUser(user: User) = userDB.deleteUser(user)

    fun getUserById(id: Long) = userDB.getUserById(id)

    fun getAllUsers() = userDB.getAllUsers()

    fun getDefaultUser() = userDB.getDefaultUser()

    fun clearUsers() = userDB.clearUsers()
}