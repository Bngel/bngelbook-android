package cn.bngel.bngelbook.dao

import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.billDao.Bill
import retrofit2.Call
import retrofit2.http.*


/**
 * @author: bngel
 * @date: 21.11.11
 * @description:
 */

interface BillDao: BasicDao {

    /**
     * Bill - 创建账单
     */
    @POST(".")
    fun postBill(@Body bill: Bill): Call<CommonResult<Boolean>>

    /**
     * Bill - 删除账单
     */
    @DELETE(".")
    fun deleteBillById(@Query("id") id: Long): Call<CommonResult<Boolean>>

    /**
     * Bill - 修改账单信息
     */
    @PUT(".")
    fun updateBillById(@Body bill: Bill): Call<CommonResult<Boolean>>

    /**
     * Bill - 查询账单
     */
    @GET(".")
    fun getBillById(@Query("id") id: Long): Call<CommonResult<Bill>>

    /**
     * Bill - 查询账本账单
     */
    @GET("book/{bookId}")
    fun getBillsByBookId(@Path("bookId") bookId: Long): Call<CommonResult<List<Bill>>>

    /**
     * Bill - 查询账户账单
     */
    @GET("account/{accountId}")
    fun getBillsByAccountId(@Path("accountId") accountId: Long): Call<CommonResult<List<Bill>>>

}