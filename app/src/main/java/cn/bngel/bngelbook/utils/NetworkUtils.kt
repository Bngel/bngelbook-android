package cn.bngel.bngelbook.utils

import android.content.Context
import android.net.ConnectivityManager
import cn.bngel.bngelbook.activity.ActivityManager

object NetworkUtils {

    fun isNetworkConnected(): Boolean {
        val context = ActivityManager.getCurActivity()
        if (context != null) {
            val mConnectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mNetworkInfo = mConnectivityManager.activeNetworkInfo
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable
            }
        }
        return false
    }

}