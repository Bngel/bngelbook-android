package cn.bngel.bngelbook.network.interceptor

import cn.bngel.bngelbook.data.GlobalVariables
import okhttp3.Interceptor
import okhttp3.Response


/**
 * @author: bngel
 * @date: 22.1.8
 * @description:
 */

class TokenInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("token", GlobalVariables.token)
            .build()
        return chain.proceed(request)
    }
}