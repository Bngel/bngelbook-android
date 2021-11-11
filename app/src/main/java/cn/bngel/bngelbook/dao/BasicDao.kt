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

        val USER_URL = "http://192.168.1.105:9001/consumer/user/"

        inline fun <reified T: BasicDao> create(url: String): T {
            val baseUrl = url
            val gson = GsonBuilder()
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