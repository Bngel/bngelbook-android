package cn.bngel.bngelbook.network

import android.util.Log
import cn.bngel.bngelbook.data.CommonResult
import com.google.gson.JsonParser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.lang.Thread.sleep


/**
 * @author: bngel
 * @date: 21.12.9
 * @description:
 */

class TimeoutInterceptor: Interceptor {

    private val times = 10

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var retryTimes = 0
        var response: Response
        do {
            response = chain.proceed(request)
            val repJson = response.peekBody(Long.MAX_VALUE).string()
            val repBody = JsonParser.parseString(repJson).asJsonObject
            val repCode = repBody.get("code").asInt
            if (repCode == 200)
                break
            retryTimes += 1
            sleep(1000)
        } while (repCode == 408 && retryTimes < times)
        return response
    }
}