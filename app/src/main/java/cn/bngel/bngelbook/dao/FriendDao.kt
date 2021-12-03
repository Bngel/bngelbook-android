package cn.bngel.bngelbook.dao

import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.friendDao.Friend
import retrofit2.Call
import retrofit2.http.*


/**
 * @author: bngel
 * @date: 21.11.28
 * @description:
 */

interface FriendDao: BasicDao {

    /**
     * Friend - 查询好友关系
     */
    @GET(".")
    fun getFriendById(@Query("id") id: Long): Call<CommonResult<Friend>>

    /**
     * Friend - 添加好友
     */
    @POST(".")
    fun addFriend(@Body friend: Friend): Call<CommonResult<Boolean>>

    /**
     * Friend - 修改好友关系
     */
    @PUT(".")
    fun updateFriendById(@Body friend: Friend): Call<CommonResult<Boolean>>

    /**
     * Friend - 删除好友
     */
    @DELETE("id")
    @FormUrlEncoded
    fun deleteFriendById(@Field("id") id: Long): Call<CommonResult<Boolean>>

    /**
     * Friend - 删除好友
     */
    @HTTP(method = "DELETE", path = ".", hasBody = true)
    fun deleteFriendByFriend(@Body friend: Friend): Call<CommonResult<Boolean>>

    /**
     * Friend - 查询好友关系
     */
    @GET("{userId}")
    fun getFriendsByUserId(@Query("userId") userId: Long): Call<CommonResult<List<Friend>>>
}