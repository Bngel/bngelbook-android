package cn.bngel.bngelbook.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.data.bean.Account
import cn.bngel.bngelbook.data.bean.Book
import cn.bngel.bngelbook.network.api.AccountApi
import cn.bngel.bngelbook.network.api.BookApi
import cn.bngel.bngelbook.ui.page.PageManager
import cn.bngel.bngelbook.ui.theme.BngelbookTheme
import cn.bngel.bngelbook.ui.widget.UiWidget

class AccountSaveActivity : BaseActivity() {

    private val loading = mutableStateOf(false)
    private val accountUpdateState = mutableStateOf(true)
    private val accountName = mutableStateOf("")
    private val accountBalance = mutableStateOf("")
    private val bookUpdateState = mutableStateOf(true)
    private val bookName = mutableStateOf("")
    private val bookType = mutableStateOf("")
    private val curType = mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        curType.value = intent.getIntExtra("type", 0)
        setContent {
            BngelbookTheme {
                Surface(color = MaterialTheme.colors.background){
                    AccountSavePage()
                    if (loading.value)
                        UiWidget.Dialog_Loading{loading.value = false}
                }
            }
        }
    }

    @Composable
    private fun AccountSavePage() {
        curType.value = intent.getIntExtra("type", 0)
        Column {
            AccountSaveTitle()
            if (curType.value == 0)
                AccountInfoField()
            else
                BookInfoField()
        }
    }

    @Composable
    private fun AccountSaveTitle() {
        Box(modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
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
                                    Intent(this@AccountSaveActivity, MainActivity::class.java)
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
                                if (curType.value == 0) {
                                    accountUpdateState.value = true
                                    postAccount()
                                }
                                else {
                                    bookUpdateState.value = true
                                    postBook()
                                }
                                PageManager.updateAllPage()
                            })
                }
            }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 5.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)) {
                    Text(text = "账户", color = if (curType.value == 0) Color(0xFFFFFFFF) else Color(0xFF66CCFF),
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color(0xFF66CCFF),
                                shape = RoundedCornerShape(1.dp)
                            )
                            .background(
                                color = if (curType.value == 0) Color(0xFF66CCFF) else Color(
                                    0xFFFFFFFF
                                )
                            )
                            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { if (curType.value != 0) curType.value = 0 })
                    Text(text = "账本", color = if (curType.value == 1) Color(0xFFFFFFFF) else Color(0xFF66CCFF),
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color(0xFF66CCFF),
                                shape = RoundedCornerShape(1.dp)
                            )
                            .background(
                                color = if (curType.value == 1) Color(0xFF66CCFF) else Color(
                                    0xFFFFFFFF
                                )
                            )
                            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { if (curType.value != 1) curType.value = 1 })
                }
            }
        }
    }

    @Composable
    private fun AccountInfoField() {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            InfoInputField(info = "账户名称", data = accountName, false)
            InfoInputField(info = "账户余额", data = accountBalance, true)
        }
    }

    @Composable
    private fun BookInfoField() {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            InfoInputField(info = "账本名称", data = bookName, false)
            InfoInputField(info = "账本类型", data = bookType, false)
        }
    }

    @Composable
    private fun InfoInputField(info: String, data: MutableState<String>, isDigit: Boolean) {
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

    private fun postBook() {
        if (bookUpdateState.value) {
            loading.value = true
            BookApi.postBook(
                Book(
                    null,
                    bookName.value,
                    GlobalVariables.USER?.id,
                    bookType.value
                )
            ) { result ->
                if (result?.code == 200) {
                    Toast.makeText(this@AccountSaveActivity, "账本创建成功", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else {
                    Toast.makeText(this@AccountSaveActivity, "账本创建失败", Toast.LENGTH_SHORT).show()
                }
                bookUpdateState.value = false
                loading.value = false
            }
        }
    }
}