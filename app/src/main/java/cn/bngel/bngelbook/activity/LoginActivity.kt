package cn.bngel.bngelbook.activity

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.Sleep
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import cn.bngel.bngelbook.data.CommonResult
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.data.bean.User
import cn.bngel.bngelbook.data.sharedPreferences.spApi
import cn.bngel.bngelbook.network.api.UserApi
import cn.bngel.bngelbook.ui.page.PageManager
import cn.bngel.bngelbook.ui.theme.BngelbookTheme
import cn.bngel.bngelbook.ui.widget.UiWidget.Dialog_Loading
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import kotlin.concurrent.thread

class LoginActivity : BaseActivity() {

    private val WAY_SMS = 0
    private val WAY_PASSWORD = 1
    private val WAY_VERIFICATION = 2
    private val loginWay = mutableStateOf(WAY_SMS)
    private val loading = mutableStateOf(false)
    private var userPhone = mutableStateOf("")
    private val SMS_CLOCK = 60
    private val smsClock = mutableStateOf(0)

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
    private fun LoginPage() {
        if (loading.value)
            Dialog_Loading { loading.value = false }
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier
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
                .padding(start = 40.dp, end = 40.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(
                        color = Color(0xFFFFFFFF),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 30.dp)
            ) {
                Text(
                    text = "Welcome", fontSize = 22.sp, modifier = Modifier
                        .padding(top = 10.dp, bottom = 30.dp)
                        .align(Alignment.CenterHorizontally)
                )
                when (loginWay.value) {
                    WAY_PASSWORD -> LoginPasswordWay()
                    WAY_SMS -> LoginSmsWay()
                    WAY_VERIFICATION -> VerificationWay()
                }
            }
        }
    }

    @Composable
    private fun VerificationWay() {
        VerificationCodeField(digits = 4, inputCallback = { code ->
            UserApi.postUserLoginCheck(userPhone.value, code) { result ->
                loading.value = true
                if (result?.code == CommonResult.SUCCESS_CODE && result.data != null) {
                    PageManager.updateAllPage()
                    loading.value = false
                    Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("loginState", true)
                    intent.putExtra("userInfo", result.data)
                    GlobalVariables.token = result.message
                    setResult(RESULT_FIRST_USER, intent)
                    finish()
                }
            }
        }) { text,focused ->
            SimpleVerificationCodeItem(text = text, focused = focused)
        }
    }

    @Composable
    private fun LoginSmsWay() {
        TextField(modifier = Modifier.padding(10.dp),
            value = userPhone.value,
            label = { Text(text = "Tel", fontSize = 12.sp) },
            singleLine = true,
            placeholder = { Text(text = "请输入手机号", fontSize = 16.sp) },
            onValueChange = {
                if (it.length <= 11)
                    userPhone.value = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Button(enabled = smsClock.value == 0 && userPhone.value.length == 11,
            modifier = Modifier.padding(top = 20.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color(0xFFFFFFFF),
                backgroundColor = Color(0xFF66CCFF)
            ),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                smsClock.value = SMS_CLOCK
                thread {
                    while (smsClock.value > 0) {
                        smsClock.value -= 1
                        Thread.sleep(1000)
                    }
                }
                UserApi.postUserLoginSms(userPhone.value) {
                    if (it?.code == CommonResult.SUCCESS_CODE)
                        Toast.makeText(this@LoginActivity, "验证码已发送", Toast.LENGTH_SHORT).show()
                }
                loginWay.value = WAY_VERIFICATION
            }) {
            Text(text =  if (smsClock.value == 0) "获取验证码" else "${smsClock.value}秒后重试")
        }
        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 10.dp)) {
            Text(text = "密码登录", textAlign = TextAlign.Start, fontSize = 13.sp,
                modifier = Modifier
                    .weight(1F)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        loginWay.value = WAY_PASSWORD
                    })
        }
    }

    @Composable
    private fun LoginPasswordWay() {
        var userAccount by remember {
            mutableStateOf("")
        }
        var userPassword by remember {
            mutableStateOf("")
        }
        TextField(modifier = Modifier.padding(10.dp),
            value = userAccount,
            label = { Text(text = "Account", fontSize = 12.sp) },
            singleLine = true,
            placeholder = { Text(text = "请输入手机号", fontSize = 16.sp) },
            onValueChange = {
                userAccount = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        TextField(modifier = Modifier.padding(10.dp),
            value = userPassword,
            label = { Text(text = "Password", fontSize = 12.sp) },
            singleLine = true,
            placeholder = { Text(text = "请输入密码", fontSize = 16.sp) },
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = {
                userPassword = it
            })
        Button(modifier = Modifier.padding(top = 20.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color(0xFFFFFFFF),
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
                                GlobalVariables.token = result.message
                                intent.putExtra("loginState", true)
                                intent.putExtra("userInfo", result.data)
                                this@LoginActivity.setResult(RESULT_FIRST_USER, intent)
                                spApi.setLocalUser(result.data?.base64)
                                spApi.setToken(result.message)
                                Toast.makeText(this@LoginActivity, "登录成功", Toast.LENGTH_SHORT)
                                    .show()
                                finish()
                            }
                            400 -> {
                                intent.putExtra("loginState", false)
                                this@LoginActivity.setResult(RESULT_OK, intent)
                                Toast.makeText(this@LoginActivity, "用户名或密码错误", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            else -> {
                                intent.putExtra("loginState", false)
                                this@LoginActivity.setResult(RESULT_OK, intent)
                                Toast.makeText(
                                    this@LoginActivity,
                                    "网络状态异常, 请稍后再试",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        intent.putExtra("loginState", false)
                        this@LoginActivity.setResult(RESULT_OK, intent)
                        Toast.makeText(this@LoginActivity, "网络状态异常, 请稍后再试", Toast.LENGTH_SHORT)
                            .show()
                    }
                    loading.value = false
                }
            }) {
            Text(text = "登录")
        }
        Row(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 10.dp)) {
            Text(text = "验证码登录", textAlign = TextAlign.Start, fontSize = 13.sp,
                modifier = Modifier
                    .weight(1F)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        loginWay.value = WAY_SMS
                    })
            Text(
                text = "找回密码", textAlign = TextAlign.End, fontSize = 13.sp,
                modifier = Modifier.weight(1F)
            )
        }
    }

    @Composable
    fun SimpleVerificationCodeItem(text: String, focused: Boolean) {
        val borderColor = if (focused) Color.Gray else Color(0xeeeeeeee)
        Box(
            modifier = Modifier
                .border(1.dp, borderColor)
                .size(50.dp, 50.dp), contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }

    }

    @Composable
    fun VerificationCodeField(
        digits: Int,
        horizontalMargin: Dp = 10.dp,
        inputCallback: (content: String) -> Unit = {},
        itemScope: @Composable (text: String, focused: Boolean) -> Unit
    ) {
        var content by remember { mutableStateOf("") }
        Box {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(digits) {
                    if (it != 0) {
                        Spacer(modifier = Modifier.width(horizontalMargin))
                    }
                    val text = content.getOrNull(it)?.toString() ?: ""
                    val focused = it == content.length
                    itemScope(text, focused)
                }
            }
            BasicTextField(
                value = content, onValueChange = {
                    if (it.length <= digits)
                        content = it
                    if (it.length == digits)
                        inputCallback(it)
                }, modifier = Modifier
                    .drawWithContent {}
                    .matchParentSize(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }

    override fun onBackPressed() {
        if (loginWay.value == WAY_VERIFICATION)
            loginWay.value = WAY_SMS
        else
            super.onBackPressed()
    }
}