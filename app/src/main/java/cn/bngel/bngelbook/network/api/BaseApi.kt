package cn.bngel.bngelbook.network.api

import android.util.Log
import cn.bngel.bngelbook.data.CommonResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketException

abstract class BaseApi {

    protected fun <T: CommonResult<D>, D> basicCallback(event: ((T?) -> Unit)? = null) = object: Callback<T> {
        override fun onResponse(
            call: Call<T>,
            response: Response<T>
        ) {
            if (event != null) {
                val body = response.body()
                event(body)
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.d("bngelbook_error", t.cause.toString())
            if (t is SocketException)
                call.clone().enqueue(this)
            else
                if (event != null) event(null)
        }
    }

}