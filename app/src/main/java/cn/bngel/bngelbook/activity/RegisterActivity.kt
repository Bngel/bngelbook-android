package cn.bngel.bngelbook.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.bngel.bngelbook.data.bean.User
import cn.bngel.bngelbook.network.UserApi
import cn.bngel.bngelbook.ui.theme.BngelbookTheme
import cn.bngel.bngelbook.ui.widget.UiWidget.Dialog_Loading

class RegisterActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BngelbookTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    RegisterPage()
                }
            }
        }
    }

    @Composable
    fun RegisterPage() {
        val loading = remember {
            mutableStateOf(false)
        }
        if (loading.value)
            Dialog_Loading { loading.value = false }
        Box(contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xff66ccff),
                        Color(0x6666ccff)
                    )
                )
            )
            .padding(start = 40.dp, end = 40.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(
                        color = Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 30.dp)) {
                Text(text = "Last Step", fontSize = 22.sp, modifier = Modifier
                    .padding(top = 10.dp, bottom = 30.dp)
                    .align(Alignment.CenterHorizontally))
                var userAccount by remember {
                    mutableStateOf("")
                }
                var userPassword by remember {
                    mutableStateOf("")
                }
                var username by remember {
                    mutableStateOf("")
                }
                TextField(modifier = Modifier.padding(10.dp),
                    value = username,
                    label = { Text(text = "Username", fontSize = 12.sp) } ,
                    singleLine = true,
                    placeholder = { Text(text = "请输入昵称", fontSize = 16.sp) },
                    onValueChange = {
                        username = it
                    })
                TextField(modifier = Modifier.padding(10.dp),
                    value = userAccount,
                    label = { Text(text = "Account", fontSize = 12.sp) } ,
                    singleLine = true,
                    placeholder = { Text(text = "请输入手机号", fontSize = 16.sp) },
                    onValueChange = {
                        userAccount = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType =  KeyboardType.Number)
                )
                TextField(modifier = Modifier.padding(10.dp),
                    value = userPassword,
                    label = { Text(text = "Password", fontSize = 12.sp) } ,
                    singleLine = true,
                    placeholder = { Text(text = "请输入密码", fontSize = 16.sp) },
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        userPassword = it
                    })
                Button(modifier = Modifier.padding(top = 20.dp),
                    colors = ButtonDefaults.buttonColors(contentColor = Color(0xFFFFFFFF),
                        backgroundColor = Color(0xFF66CCFF)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        loading.value = true
                        val account = userAccount
                        val password = userPassword
                        val usernameText = username
                        UserApi.registerUserByPhone(
                            User(null,null,null,null,password,
                                account,null,null,usernameText)
                        ) { result ->
                            if (result != null) loading.value = false
                            when (result?.code) {
                                200 -> {
                                    Toast.makeText(this@RegisterActivity, "注册成功", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                411 -> {
                                    Toast.makeText(this@RegisterActivity, "手机号已被注册", Toast.LENGTH_SHORT).show()
                                }
                                400 -> {
                                    Toast.makeText(this@RegisterActivity, "注册失败", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }){
                    Text(text = "注册")
                }
            }
        }
    }
}
