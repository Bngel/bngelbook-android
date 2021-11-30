package cn.bngel.bngelbook

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.edit
import cn.bngel.bngelbook.network.UserApi
import cn.bngel.bngelbook.ui.theme.BngelbookTheme
import com.google.accompanist.glide.rememberGlidePainter
import cn.bngel.bngelbook.ui.widget.UiWidget.Dialog_Loading

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BngelbookTheme {
                Surface(color = MaterialTheme.colors.background) {
                    LoginPage()
                }
            }
        }
    }

    @Composable
    fun LoginPage() {
        var loading = remember {
            mutableStateOf(false)
        }
        if (loading.value)
            Dialog_Loading()
        Scaffold{
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
                    Text(text = "Welcome", fontSize = 22.sp, modifier = Modifier
                        .padding(top = 10.dp, bottom = 30.dp)
                        .align(Alignment.CenterHorizontally))
                    var userAccount by remember {
                        mutableStateOf("")
                    }
                    var userPassword by remember {
                        mutableStateOf("")
                    }
                    TextField(modifier = Modifier.padding(10.dp),
                        value = userAccount,
                        label = { Text(text = "Account", fontSize = 12.sp) } ,
                        singleLine = true,
                        placeholder = { Text(text = "请输入手机号/邮箱", fontSize = 16.sp) },
                        onValueChange = {
                            userAccount = it
                        })
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
                            UserApi.postUserLogin(account, password) { result ->
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                if (result != null) {
                                    when (result.code) {
                                        200 -> {
                                            intent.putExtra("loginState", true)
                                            intent.putExtra("userInfo", result.data)
                                            this@LoginActivity.setResult(RESULT_FIRST_USER, intent)
                                            getSharedPreferences("loginState", MODE_PRIVATE).edit {
                                                putBoolean("state", true)
                                                putString("account", account)
                                                putString("password", password)
                                            }
                                            Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                                            finish()
                                        }
                                        400 -> {
                                            intent.putExtra("loginState", false)
                                            this@LoginActivity.setResult(RESULT_OK, intent)
                                            Toast.makeText(this@LoginActivity, "用户名或密码错误", Toast.LENGTH_SHORT).show()
                                        }
                                        else -> {
                                            intent.putExtra("loginState", false)
                                            this@LoginActivity.setResult(RESULT_OK, intent)
                                            Toast.makeText(this@LoginActivity, "网络状态异常, 请稍后再试", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                                else {
                                    intent.putExtra("loginState", false)
                                    this@LoginActivity.setResult(RESULT_OK, intent)
                                    Toast.makeText(this@LoginActivity, "网络状态异常, 请稍后再试", Toast.LENGTH_SHORT).show()
                                }
                                loading.value = false
                            }
                        }) {
                        Text(text = "登录")
                    }
                    Row(modifier = Modifier.padding(10.dp)) {
                        Text(text = "注册账号", textAlign = TextAlign.Start, fontSize = 13.sp,
                            modifier = Modifier.weight(1F))
                        Text(text = "找回密码", textAlign = TextAlign.End, fontSize = 13.sp,
                            modifier = Modifier.weight(1F))
                    }
                }
            }
        }
    }

}