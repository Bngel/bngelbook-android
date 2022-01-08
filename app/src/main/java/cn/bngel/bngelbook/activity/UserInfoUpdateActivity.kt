package cn.bngel.bngelbook.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.bngel.bngelbook.R
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.data.bean.User
import cn.bngel.bngelbook.network.api.UserApi
import cn.bngel.bngelbook.ui.page.PageManager
import cn.bngel.bngelbook.ui.theme.BngelbookTheme
import cn.bngel.bngelbook.ui.widget.UiWidget
import cn.bngel.bngelbook.utils.UiUtils
import androidx.core.content.FileProvider

import android.os.Build
import android.util.Log
import cn.bngel.bngelbook.utils.TencentcloudUtils
import com.bumptech.glide.load.resource.file.FileDecoder
import java.io.File
import java.lang.NullPointerException
import java.net.URI
import android.provider.MediaStore

import android.provider.DocumentsContract

import android.content.ContentUris

import android.os.Environment
import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.snapshot.UserState
import com.github.dhaval2404.imagepicker.ImagePicker


class UserInfoUpdateActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BngelbookTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    UserInfoUpdatePage()
                }
            }
        }
    }

    @Composable
    private fun UserInfoUpdatePage() {
        Column {
            UserInfoUpdateTitle()
            UserInfoUpdateItems()
        }
    }

    @Composable
    private fun UserInfoUpdateTitle() {
        Box(modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.Start) {
                    Image(imageVector = Icons.Filled.Close, contentDescription = "close_btn",
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 15.dp)
                            .width(30.dp)
                            .height(30.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                val intent =
                                    Intent(this@UserInfoUpdateActivity, MainActivity::class.java)
                                setResult(RESULT_CANCELED, intent)
                                finish()
                                UserState.reload()
                            })
                }
                Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.End) {
                    Image(imageVector = Icons.Filled.Check, contentDescription = "ok_btn",
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 15.dp)
                            .width(30.dp)
                            .height(30.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                updateUser()
                                PageManager.updateAllPage()
                            })
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp), horizontalArrangement = Arrangement.Center) {
                Text(text = "编辑账号信息", fontSize = 18.sp)
            }
        }
    }

    @Composable
    private fun UserInfoUpdateItems() {
        Column(modifier = Modifier.fillMaxWidth()) {
            UserInfoUpdateItemForProfile(modifier = Modifier.padding(top = 15.dp, bottom = 15.dp))
            UserInfoUpdateItemForUsername()
            UserInfoUpdateItemForGender(modifier = Modifier.padding(top = 15.dp, bottom = 15.dp))
            UserInfoUpdateItemForBirthday(modifier = Modifier.padding(top = 15.dp, bottom = 15.dp))
        }
    }

    @Composable
    private fun UserInfoUpdateItemForProfile(modifier: Modifier = Modifier) {

        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
            Text(text = "头像", fontSize = 13.sp, textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F))
            Row(modifier = Modifier.weight(2F), verticalAlignment = Alignment.CenterVertically) {
                UiWidget.CustomProfileImage(
                    filePath = UserState.profile.value,
                    modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 15.dp)
                        .width(60.dp)
                        .height(60.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            ImagePicker.with(this@UserInfoUpdateActivity)
                                .cropSquare()
                                .galleryMimeTypes(  //Exclude gif images
                                    mimeTypes = arrayOf(
                                        "image/png",
                                        "image/jpg",
                                        "image/jpeg"
                                    )
                                )
                                .galleryOnly()
                                .createIntent { intent ->
                                    launcher.launch(intent)
                                }
                        })
            }
        }
    }

    @Composable
    private fun UserInfoUpdateItemForUsername(modifier: Modifier = Modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
            Text(text = "用户名", fontSize = 13.sp, textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F))
            TextField(value = UserState.username.value, onValueChange = {UserState.username.value = it},
                textStyle = TextStyle(fontSize = 13.sp),colors = TextFieldDefaults.textFieldColors(
                    disabledTextColor = Color.Transparent,
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent),
                maxLines = 1, singleLine = true,
                modifier = Modifier
                    .weight(2F)
            )
        }
    }

    @Composable
    private fun UserInfoUpdateItemForGender(modifier: Modifier = Modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
            Text(text = "性别", fontSize = 13.sp, textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F))
            Row(modifier = Modifier.weight(2F)) {
                Row(modifier = Modifier.weight(1F), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = UserState.gender.value == 1, onClick = { UserState.gender.value = 1 })
                    Text(text = "男", fontSize = 13.sp, textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 5.dp))
                }
                Row(modifier = Modifier.weight(1F), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = UserState.gender.value == 0, onClick = { UserState.gender.value = 0 })
                    Text(text = "女", fontSize = 13.sp, textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 5.dp))
                }
            }
        }
    }

    @Composable
    private fun UserInfoUpdateItemForBirthday(modifier: Modifier = Modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
            Text(text = "出生日期", fontSize = 13.sp, textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F))
            Text(text = UserState.birthday.value, fontSize = 13.sp, textAlign = TextAlign.Start,
                modifier = Modifier.weight(2F))
        }
    }

    private fun updateUser() {
        val user = User(
            id = UserState.id.value,
            username = UserState.username.value,
            gender = UserState.gender.value,
            birthday = UserState.birthday.value
        )
        UserApi.updateUserById(user) { result ->
            if (result?.code == 200) {
                GlobalVariables.USER?.apply {
                    username = UserState.username.value
                    gender = UserState.gender.value
                    birthday = UserState.birthday.value
                }
                val intent = Intent(this@UserInfoUpdateActivity, UserDetailActivity::class.java)
                if (UserState.profile.value != GlobalVariables.getDefaultProfile()) {
                    val file = File(UserState.profile.value)
                    UserApi.postUserProfile(
                        GlobalVariables.USER?.id!!,
                        file
                    ) { profileResult ->
                        if (profileResult == null) {
                            setResult(RESULT_CANCELED)
                            finish()
                            UserState.reload()
                        }
                        if (profileResult?.code == CommonResult.SUCCESS_CODE) {
                            GlobalVariables.USER?.profile = profileResult.data
                            val originProfile = File(GlobalVariables.getDefaultProfile())
                            File(UserState.profile.value).copyTo(originProfile, true)
/*                            File(UserState.profile.value).delete()
                            UserState.profile.value = GlobalVariables.getDefaultProfile()*/
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                    }
                }
            }
            else {
                Toast.makeText(this@UserInfoUpdateActivity, "修改信息失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun event(result: ActivityResult) {
        super.event(result)
        val data = result.data
        if (result.resultCode == RESULT_OK) {
            val uri: Uri = data?.data!!
            val imageFile = File(uri.path?:"")
            if (imageFile.exists()) {
                UserState.profile.value = imageFile.path
            }
        }
    }
}
