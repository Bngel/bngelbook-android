package cn.bngel.bngelbook.network

import android.util.Log
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
            Log.d("bngelbook_error", t.cause.toString())
            if (event != null) {
                event(null)
            }
        }
    }

}