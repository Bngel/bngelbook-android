package cn.bngel.bngelbook.dao

import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.bean.Version
import retrofit2.Call
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


/**
 * @author: bngel
 * @date: 22.1.12
 * @description:
 */

interface VersionDao: BasicDao {

    /**
     * Version - 获取最新版本信息
     */
    @GET("newest")
    fun getNewestVersion(): Call<CommonResult<Version>>

}