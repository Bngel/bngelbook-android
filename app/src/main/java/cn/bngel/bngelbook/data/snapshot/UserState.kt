package cn.bngel.bngelbook.data.snapshot

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.utils.NetworkUtils
import java.io.File

object UserState {

    val id by lazy { mutableStateOf(GlobalVariables.USER?.id?:-1L ) }
    val username by lazy { mutableStateOf(GlobalVariables.USER?.username?:"") }
    val birthday by lazy { mutableStateOf(GlobalVariables.USER?.birthday?:"") }
    val email by lazy { mutableStateOf(GlobalVariables.USER?.email?:"") }
    val gender by lazy { mutableStateOf(GlobalVariables.USER?.gender?:1) }
    val phone by lazy { mutableStateOf(GlobalVariables.USER?.phone?:"") }
    val profile by lazy { mutableStateOf(GlobalVariables.USER?.phone?:"") }
    val profileImage by lazy {
        val defaultProfile = GlobalVariables.getDefaultProfile()
        val profileFile = File(defaultProfile)
        if (profileFile.exists())
            profileFile.delete()
        mutableStateOf(defaultProfile)
    }


    fun reload() {
        id.value = GlobalVariables.USER?.id?:-1L
        username.value = GlobalVariables.USER?.username?:""
        birthday.value = GlobalVariables.USER?.birthday?:""
        email.value = GlobalVariables.USER?.email?:""
        gender.value = GlobalVariables.USER?.gender?:1
        phone.value = GlobalVariables.USER?.phone?:""
        profile.value = GlobalVariables.USER?.profile?:""
    }

}