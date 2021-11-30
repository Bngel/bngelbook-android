package cn.bngel.bngelbook.activity

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts


object ActivityManager {

    private var curActivity: BaseActivity?  = null

    fun getCurActivity() = curActivity
    fun setCurActivity(activity: BaseActivity){ curActivity = activity }

}