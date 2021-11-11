package cn.bngel.bngelbook.network

import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.userDao.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseApi {

    protected fun <T> basicCallback(event: ((T?) -> Unit)? = null) = object: Callback<T> {
        override fun onResponse(
            call: Call<T>,
            response: Response<T>
        ) {
            if (event != null)
                event(response.body())
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            t.printStackTrace()
            if (event != null)
                event(null)
        }
    }

}