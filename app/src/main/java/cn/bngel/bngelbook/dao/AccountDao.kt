package cn.bngel.bngelbook.dao

import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.accountDao.Account
import retrofit2.Call
import retrofit2.http.*


/**
 * @author: bngel
 * @date: 21.11.19
 * @description:
 */

interface AccountDao: BasicDao {

    /**
     * Account - 创建账户
     */
    @POST(".")
    fun postAccount(@Body account: Account): Call<CommonResult<Boolean>>

    /**
     * Account - 删除账户
     */
    @DELETE(".")
    @FormUrlEncoded
    fun deleteAccountById(@Field("id") id: Long): Call<CommonResult<Boolean>>

    /**
     * Account - 修改账户信息
     */
    @PUT(".")
    fun updateAccountById(@Body account: Account): Call<CommonResult<Boolean>>

    /**
     * Account - 查询账户
     */
    @GET(".")
    fun getAccountById(@Query("id") id: Long): Call<CommonResult<Account>>

    /**
     * Account - 查询用户账户
     */
    @GET("{userId}")
    fun getAccountsByUserId(@Path("userId") userId: Long): Call<CommonResult<List<Account>>>
}