package cn.bngel.bngelbook.network.api

import cn.bngel.bngelbook.dao.BasicDao
import cn.bngel.bngelbook.dao.UserDao
import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.bean.User
import cn.bngel.bngelbook.utils.NetworkUtils

object UserApi: BaseApi() {

    private val userService by lazy {
        BasicDao.create<UserDao>(BasicDao.USER_URL)
    }

    fun postUserLogin(account: String,
                      password: String, event: ((CommonResult<User>?) -> Unit)? = null) {
        if (!NetworkUtils.isNetworkConnected()) return
        userService.postUserLogin(account, password).enqueue(basicCallback(event))
    }

    fun postUser(user: User, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        if (!NetworkUtils.isNetworkConnected()) return
        userService.postUser(user).enqueue(basicCallback(event))
    }

    fun registerUserByPhone(user: User, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        if (!NetworkUtils.isNetworkConnected()) return
        userService.registerUser(user, 0).enqueue(basicCallback(event))
    }

    fun deleteUserById(id: Long, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        if (!NetworkUtils.isNetworkConnected()) return
        userService.deleteUserById(id).enqueue(basicCallback(event))
    }

    fun updateUserById(user: User, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        if (!NetworkUtils.isNetworkConnected()) return
        userService.updateUserById(user).enqueue(basicCallback(event))
    }

    fun getUserById(id: Long, event: ((CommonResult<User>?) -> Unit)? = null) {
        if (!NetworkUtils.isNetworkConnected()) return
        userService.getUserById(id).enqueue(basicCallback(event))
    }

    fun getUserRegisterDays(id: Long, event: ((CommonResult<Int>?) -> Unit)? = null) {
        if (!NetworkUtils.isNetworkConnected()) return
        userService.getUserRegisterDays(id).enqueue(basicCallback(event))
    }

    fun getFriendsById(id: Long, event: ((CommonResult<List<User>>?) -> Unit)? = null) {
        if (!NetworkUtils.isNetworkConnected()) return
        userService.getFriendsById(id).enqueue(basicCallback(event))
    }

    fun getUsersByUsername(username: String, event: ((CommonResult<List<User>>?) -> Unit)? = null){
        if (!NetworkUtils.isNetworkConnected()) return
        userService.getUsersByUsername(username).enqueue(basicCallback(event))
    }

}