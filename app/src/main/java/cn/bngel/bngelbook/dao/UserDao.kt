package cn.bngel.bngelbook.dao

import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.userDao.User
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


/**
 * @author: bngel
 * @date: 21.11.10
 * @description:
 */

interface UserDao: BasicDao {

    /**
     * User - 用户登录
     */
    @FormUrlEncoded
    @POST("login")
    fun postUserLogin(
        @Field("account") account: String,
        @Field("password") password: String
    ): Call<CommonResult<User>>

    /**
     * User - 查询用户
     */
    @GET(".")
    fun getUserById(
        @Query("id") id: Long
    ): Call<CommonResult<User>>

    /**
     * User - 创建用户
     */
    @POST(".")
    fun postUser(@Body user: User): Call<CommonResult<Boolean>>

    /**
     * User - 注销用户
     */
    @DELETE(".")
    fun deleteUserById(@Field("id") id: Long): Call<CommonResult<Boolean>>

    /**
     * User - 修改用户信息
     */
    @PUT(".")
    fun updateUserById(@Body user: User): Call<CommonResult<Boolean>>

    /**
     * User - 查询用户注册时间长度
     */
    @GET("registerDays")
    fun getUserRegisterDays(@Query("id") id: Long): Call<CommonResult<Int>>

    /**
     * User - 获取好友列表
     */
    @GET("friends")
    fun getFriendsById(@Query("id") id: Long): Call<CommonResult<List<User>>>
}