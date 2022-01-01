package cn.bngel.bngelbook.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
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

class UserInfoUpdateActivity : BaseActivity() {

    private val udBirthday = mutableStateOf("")
    private val udGender = mutableStateOf(0)
    private val udUsername = mutableStateOf("")
    private val udProfile = mutableStateOf("")

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
        val user = GlobalVariables.USER
        user?.apply {

            udProfile.value = profile?:""
            udUsername.value = username?:""
            udBirthday.value = birthday?:""
            udGender.value = gender?:1

            Column(modifier = Modifier.fillMaxWidth()) {
                UserInfoUpdateItemForProfile(modifier = Modifier.padding(top = 15.dp, bottom = 15.dp))
                UserInfoUpdateItemForUsername()
                UserInfoUpdateItemForGender(modifier = Modifier.padding(top = 15.dp, bottom = 15.dp))
                UserInfoUpdateItemForBirthday(modifier = Modifier.padding(top = 15.dp, bottom = 15.dp))
            }
        }
    }
    
    @Composable
    private fun UserInfoUpdateItemForProfile(modifier: Modifier = Modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
            Text(text = "头像", fontSize = 13.sp, textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F))
            Row(modifier = Modifier.weight(2F), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.default_profile), contentDescription = null,
                    modifier = Modifier
                        .height(60.dp)
                        .width(60.dp))
            }
        }
    }

    @Composable
    private fun UserInfoUpdateItemForUsername(modifier: Modifier = Modifier) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
            Text(text = "用户名", fontSize = 13.sp, textAlign = TextAlign.Center,
                modifier = Modifier.weight(1F))
            TextField(value = udUsername.value, onValueChange = {udUsername.value = it},
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
                    RadioButton(selected = udGender.value == 1, onClick = { udGender.value = 1 })
                    Text(text = "男", fontSize = 13.sp, textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 5.dp))
                }
                Row(modifier = Modifier.weight(1F), verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = udGender.value == 0, onClick = { udGender.value = 0 })
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
            Text(text = udBirthday.value, fontSize = 13.sp, textAlign = TextAlign.Start,
                modifier = Modifier.weight(2F))
        }
    }

    private fun updateUser() {
        val user = User(
            id = GlobalVariables.USER?.id,
            username = udUsername.value,
            gender = udGender.value,
            birthday = udBirthday.value
        )
        UserApi.updateUserById(user) { result ->
            if (result?.code == 200) {
                GlobalVariables.USER?.apply {
                    username = udUsername.value
                    gender = udGender.value
                    birthday = udBirthday.value
                }
                val intent = Intent(this@UserInfoUpdateActivity, UserDetailActivity::class.java)
                intent.putExtra("user", GlobalVariables.USER)
                setResult(RESULT_OK, intent)
                finish()
            }
            else {
                Toast.makeText(this@UserInfoUpdateActivity, "修改信息失败", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
