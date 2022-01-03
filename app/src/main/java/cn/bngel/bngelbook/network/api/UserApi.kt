package cn.bngel.bngelbook.network.api

import cn.bngel.bngelbook.dao.BasicDao
import cn.bngel.bngelbook.dao.UserDao
import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.bean.User
import cn.bngel.bngelbook.utils.NetworkUtils
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

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

    fun registerUserByPhone(user: User, event: ((CommonResult<Boolean>?) -> Unit)? = null) {
        userService.registerUser(user, 0).enqueue(basicCallback(event))
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

    fun getUserRegisterDays(id: Long, event: ((CommonResult<Int>?) -> Unit)? = null) {
        userService.getUserRegisterDays(id).enqueue(basicCallback(event))
    }

    fun getFriendsById(id: Long, event: ((CommonResult<List<User>>?) -> Unit)? = null) {
        userService.getFriendsById(id).enqueue(basicCallback(event))
    }

    fun getUsersByUsername(username: String, event: ((CommonResult<List<User>>?) -> Unit)? = null){
        userService.getUsersByUsername(username).enqueue(basicCallback(event))
    }

    fun postUserLoginSms(phone: String, area: String = "+86", event: ((CommonResult<String>?) -> Unit)? = null) {
        userService.postUserLoginSms(area, phone).enqueue(basicCallback(event))
    }

    fun postUserLoginCheck(phone: String, code: String, event: ((CommonResult<User>?) -> Unit)? = null) {
        userService.postUserLoginCheck(code, phone).enqueue(basicCallback(event))
    }

    fun postUserProfile(id: Long, profile: File, event: ((CommonResult<String>?) -> Unit)? = null) {
        val profileBody = RequestBody.create(MediaType.parse("image/*"), profile)
        val formData = MultipartBody.Part.createFormData("profile", profile.name, profileBody)
        userService.postUserProfile(id, formData).enqueue(basicCallback(event))
    }

}