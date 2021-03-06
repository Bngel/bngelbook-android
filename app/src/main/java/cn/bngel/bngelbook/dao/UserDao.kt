package cn.bngel.bngelbook.dao

import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.bean.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


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
     * User - 注册用户
     */
    @POST("register/{type}")
    fun registerUser(@Body user: User, @Path("type") type: Int): Call<CommonResult<Boolean>>

    /**
     * User - 注销用户
     */
    @DELETE(".")
    fun deleteUserById(@Query("id") id: Long): Call<CommonResult<Boolean>>

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

    /**
     * User - 查询用户列表 By 用户名
     */
    @GET("{username}")
    fun getUsersByUsername(@Path("username") username: String): Call<CommonResult<List<User>>>

    /**
     * User - 发送登录短信
     */
    @FormUrlEncoded
    @POST("login/sms")
    fun postUserLoginSms(
        @Field("area") area: String,
        @Field("phone") phone: String
    ): Call<CommonResult<String>>

    /**
     * User - 验证码验证
     */
    @FormUrlEncoded
    @POST("login/check")
    fun postUserLoginCheck(
        @Field("code") code: String,
        @Field("phone") phone: String
    ): Call<CommonResult<User>>

    /**
     * User - 更新用户头像
     */
    @Multipart
    @POST("profile")
    fun postUserProfile(
        @Part("id") id: Long,
        @Part profile: MultipartBody.Part
    ): Call<CommonResult<String>>
}