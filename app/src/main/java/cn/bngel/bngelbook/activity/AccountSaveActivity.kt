package cn.bngel.bngelbook.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.bngel.bngelbook.R
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.data.accountDao.Account
import cn.bngel.bngelbook.network.AccountApi
import cn.bngel.bngelbook.ui.page.PageAccount
import cn.bngel.bngelbook.ui.theme.BngelbookTheme
import cn.bngel.bngelbook.ui.widget.UiWidget

class AccountSaveActivity : BaseActivity() {

    private val loading = mutableStateOf(false)
    private val accountUpdateState = mutableStateOf(true)
    private val accountName = mutableStateOf("")
    private val accountBalance = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BngelbookTheme {
                Surface(color = MaterialTheme.colors.background){
                    AccountSavePage()
                    if (loading.value)
                        UiWidget.Dialog_Loading()
                }
            }
        }
    }

    @Composable
    fun AccountSavePage() {
        Column {
            AccountSaveTitle()
            AccountInfoField()
        }
    }

    @Composable
    fun AccountSaveTitle() {
        Box(modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.Start) {
                    Image(painter = painterResource(id = R.drawable.close), contentDescription = "close_btn",
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 15.dp)
                            .width(30.dp)
                            .height(30.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                val intent =
                                    Intent(this@AccountSaveActivity, MainActivity::class.java)
                                setResult(RESULT_CANCELED, intent)
                                finish()
                            })
                }
                Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.End) {
                    Image(painter = painterResource(id = R.drawable.ok), contentDescription = "ok_btn",
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 15.dp)
                            .width(30.dp)
                            .height(30.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                accountUpdateState.value = true
                                postAccount()
                            })
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp), horizontalArrangement = Arrangement.Center) {
                Text(text = "创建账户", fontSize = 18.sp)
            }
        }
    }

    @Composable
    fun AccountInfoField() {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AccountInfoInput(info = "账户名称", data = accountName, false)
            AccountInfoInput(info = "账户余额", data = accountBalance, true)
        }
    }

    @Composable
    fun AccountInfoInput(info: String, data: MutableState<String>, isDigit: Boolean) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically) {
            TextField(value = data.value, onValueChange = { newName ->
                data.value = newName
            }, modifier = Modifier
                .weight(1F),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color(0x00000000)),
                maxLines = 1, singleLine = true, label = { Text(text = info) },
                keyboardOptions = KeyboardOptions(keyboardType = if (isDigit)
                    KeyboardType.Number else KeyboardType.Text)
            )
        }
    }

    private fun postAccount() {
        if (accountUpdateState.value) {
            loading.value = true
            try {
                AccountApi.postAccount(
                    Account(
                        null,
                        accountName.value,
                        GlobalVariables.USER?.id,
                        accountBalance.value.toDouble()
                    )
                ) { result ->
                    if (result?.code == 200) {
                        Toast.makeText(this@AccountSaveActivity, "账户创建成功", Toast.LENGTH_SHORT).show()
                        PageAccount.setUpdate(true)
                        finish()
                    }
                    else {
                        Toast.makeText(this@AccountSaveActivity, "账户创建失败", Toast.LENGTH_SHORT).show()
                    }
                    accountUpdateState.value = false
                    loading.value = false
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
                Toast.makeText(this@AccountSaveActivity, "请输入正确的余额", Toast.LENGTH_SHORT).show()
                loading.value = false
            }
        }
    }
}