package cn.bngel.bngelbook.dao

import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.bean.Book
import retrofit2.Call
import retrofit2.http.*


/**
 * @author: bngel
 * @date: 21.11.23
 * @description:
 */

interface BookDao: BasicDao {

    /**
     * Book - 创建账本
     */
    @POST(".")
    fun saveBook(@Body book: Book): Call<CommonResult<Boolean>>

    /**
     * Book - 删除账本
     */
    @DELETE(".")
    @FormUrlEncoded
    fun deleteBookById(@Field("id") id: Long): Call<CommonResult<Boolean>>

    /**
     * Book - 修改账本信息
     */
    @PUT(".")
    fun updateBookById(@Body book: Book): Call<CommonResult<Boolean>>

    /**
     * Book - 查询账本
     */
    @GET(".")
    fun getBookById(@Query("id") id: Long): Call<CommonResult<Book>>

    /**
     * Book - 查询用户账本
     */
    @GET("{userId}")
    fun getBooksByUserId(@Path("userId") userId: Long): Call<CommonResult<List<Book>>>
}