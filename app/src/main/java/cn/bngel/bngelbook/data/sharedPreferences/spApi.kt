package cn.bngel.bngelbook.data.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import cn.bngel.bngelbook.activity.ActivityManager
import cn.bngel.bngelbook.activity.BaseActivity
import cn.bngel.bngelbook.data.bean.Bean
import cn.bngel.bngelbook.data.bean.User

object spApi {

    private val LOCAL_DATA = "LOCAL_DATA"

    private fun getContext() = ActivityManager.getCurActivity() as BaseActivity

    private fun readLocalData(readEventListener: SharedPreferences.() -> Unit) {
        getContext().getSharedPreferences(LOCAL_DATA, Context.MODE_PRIVATE).apply {
            readEventListener()
        }
    }

    private fun writeLocalData(writeEventListener: SharedPreferences.Editor.() -> Unit) {
        getContext().getSharedPreferences(LOCAL_DATA, Context.MODE_PRIVATE).edit {
            writeEventListener()
            apply()
        }
    }

    fun setLocalUser(user: String?) = writeLocalData {
        putString("user", user)
    }

    fun setLocalUser(user: User?) = writeLocalData {
        putString("user", user?.base64)
    }

    fun clearLocalUser() = writeLocalData {
        putString("user", null)
    }

    fun setToken(token: String) = writeLocalData {
        putString("token", token)
    }

    fun getLocalUser(): User? {
        var user: User? = null
        readLocalData {
            val userBase64 = getString("user", "")
            if (userBase64 != null && userBase64 != "") {
                user = Bean.fromCustomBase64(userBase64)
            }
        }
        return user
    }

    fun getToken(): String {
        var token = ""
        readLocalData {
            token = getString("token", "")?:""
        }
        return token
    }
}