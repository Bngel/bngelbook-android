package cn.bngel.bngelbook.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts


/**
 * @author: bngel
 * @date: 21.11.30
 * @description:
 */

abstract class BaseActivity: ComponentActivity() {

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        event(it)
    }

    override fun onResume() {
        super.onResume()
        ActivityManager.setCurActivity(this)
    }

    protected open fun event(result: ActivityResult) {

    }
}