package cn.bngel.bngelbook.dao

import cn.bngel.bngelbook.network.interceptor.TimeoutInterceptor
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author: bngel
 * @date: 21.11.10
 * @description:
 */

interface BasicDao {

    companion object {

        private const val BASE_URL = "http://192.168.1.103"
        const val USER_URL = "$BASE_URL:9001/consumer/user/"
        const val BOOK_URL = "$BASE_URL:9002/consumer/book/"
        const val ACCOUNT_URL = "$BASE_URL:9003/consumer/account/"
        const val BILL_URL = "$BASE_URL:9004/consumer/bill/"
        const val FRIEND_URL = "$BASE_URL:9005/consumer/friend/"

        inline fun <reified T: BasicDao> create(url: String): T {
            val baseUrl = url
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd hh:mm:ss")
                .setLenient()
                .create()

            val mHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(TimeoutInterceptor())
                .build()

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(mHttpClient)
                .build()
            return retrofit.create(T::class.java)
        }
    }
}