package cn.bngel.bngelbook.network

import cn.bngel.bngelbook.dao.BasicDao
import cn.bngel.bngelbook.dao.UserDao
import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.userDao.User
import retrofit2.Call

object UserApi: BaseApi() {

    private val userService by lazy {
        BasicDao.create<UserDao>(BasicDao.USER_URL)
    }

    fun postUserLogin(account: String,
                      password: String, event: ((CommonResult<User>?) -> Unit)? = null) {
        userService.postUserLogin(account, password).enqueue(basicCallback(event))
    }

    fun postUser(user: User, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        userService.postUser(user).enqueue(basicCallback(event))
    }

    fun deleteUserById(id: Long, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        userService.deleteUserById(id).enqueue(basicCallback(event))
    }

    fun updateUserById(user: User, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        userService.updateUserById(user).enqueue(basicCallback(event))
    }

    fun getUserById(id: Long, event: ((CommonResult<User>?) -> Unit)? = null) {
        userService.getUserById(id).enqueue(basicCallback(event))
    }

}