package cn.bngel.bngelbook.dao

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

        private const val BASE_URL = "http://192.168.1.109"
        const val USER_URL = "$BASE_URL:9001/consumer/user/"
        const val BOOK_URL = "$BASE_URL:9002/consumer/book"
        const val ACCOUNT_URL = "$BASE_URL:9003/consumer/account/"
        const val BILL_URL = "$BASE_URL:9004/consumer/bill/"

        inline fun <reified T: BasicDao> create(url: String): T {
            val baseUrl = url
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .setLenient()
                .create()

            val mHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
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