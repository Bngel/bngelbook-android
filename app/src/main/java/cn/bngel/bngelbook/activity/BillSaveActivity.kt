package cn.bngel.bngelbook.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.text.isDigitsOnly
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.data.bean.Account
import cn.bngel.bngelbook.data.bean.Bill
import cn.bngel.bngelbook.network.api.AccountApi
import cn.bngel.bngelbook.network.api.BillApi
import cn.bngel.bngelbook.utils.UiUtils
import cn.bngel.bngelbook.ui.theme.BngelbookTheme
import cn.bngel.bngelbook.ui.widget.UiWidget

class BillSaveActivity : BaseActivity() {

    private val curType = mutableStateOf("吃喝")
    private val curTags = mutableStateOf("")
    private val curBalance = mutableStateOf("")
    private val curIo = mutableStateOf(0)
    private val curAccount = mutableStateOf<Long?>(null)
    private val curAccountName = mutableStateOf("不选账户")
    private var curPoint = -1

    private val accountDialogState = mutableStateOf(false)
    private val accountList = mutableStateListOf<Account>()
    private val loading = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BngelbookTheme {
                Surface(color = MaterialTheme.colors.background) {
                    BillSavePage()
                    if (loading.value)
                        UiWidget.Dialog_Loading{loading.value = false}
                }
            }
        }
    }

    @Composable
    private fun BillSavePage(){
        Column {
            BillSaveTitle()
            BillBalanceRow()
            BillTypeChoices(types = listOf("吃喝","交通","服饰","日用品","娱乐","医疗","其他"),size = 5,
                modifier = Modifier.weight(1F))
            TagsRow(tags = listOf("早餐","午餐","晚餐","超市"))
            BottomRow(account = curAccountName.value)
            Calculator()
        }
    }

    @Composable
    private fun BillSaveTitle() {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.Start) {
                    Image(imageVector = Icons.Filled.Close, contentDescription = "close_btn",
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)
                            .width(30.dp)
                            .height(30.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                val intent = Intent(this@BillSaveActivity, MainActivity::class.java)
                                setResult(RESULT_CANCELED, intent)
                                finish()
                            })
                }
                Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.End) {
                    Image(imageVector = Icons.Filled.Check, contentDescription = "ok_btn",
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)
                            .width(30.dp)
                            .height(30.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                postBill()
                            })
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Row(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 10.dp, bottom = 10.dp)) {
                    Text(text = "收入", color = if (curIo.value == 1) Color(0xFFFFFFFF) else Color(0xFF66CCFF),
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color(0xFF66CCFF),
                                shape = RoundedCornerShape(1.dp)
                            )
                            .background(
                                color = if (curIo.value == 1) Color(0xFF66CCFF) else Color(
                                    0xFFFFFFFF
                                )
                            )
                            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { if (curIo.value != 1) curIo.value = 1 })
                    Text(text = "支出", color = if (curIo.value == 0) Color(0xFFFFFFFF) else Color(0xFF66CCFF),
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color(0xFF66CCFF),
                                shape = RoundedCornerShape(1.dp)
                            )
                            .background(
                                color = if (curIo.value == 0) Color(0xFF66CCFF) else Color(
                                    0xFFFFFFFF
                                )
                            )
                            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { if (curIo.value != 0) curIo.value = 0 })
                }
            }
        }
    }

    @Composable
    private fun BillBalanceRow() {
        Row(modifier = Modifier
            .border(width = 1.dp, color = Color.Gray.copy(alpha = 0.5F))
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.weight(1F), verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = UiUtils.getTypeImg(curType.value)), contentDescription = "balance",
                    modifier = Modifier.padding(5.dp))
                Text(text = curType.value, fontSize = 20.sp, modifier = Modifier.padding(start = 10.dp, end = 10.dp))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = if (curBalance.value == "") "0" else curBalance.value, fontSize = 20.sp,
                    modifier = Modifier.padding(10.dp))
            }
        }
    }

    @Composable
    private fun BillTypeChoices(types: List<String>, size: Int, modifier: Modifier = Modifier) {
        Column(modifier = modifier.padding(top = 10.dp, bottom = 10.dp)) {
            for (i in 0 until (if (types.size % size == 0) types.size/size else types.size/size+1)) {
                Row {
                    for (j in 0 until size) {
                        if (i * size + j >= types.size)
                            Spacer(modifier = Modifier.weight(1F))
                        else {
                            BillTypeChoice(
                                type = types[i * size + j],
                                modifier = Modifier.weight(1F)
                            ) {
                                curType.value = types[i * size + j]
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun BillTypeChoice(type: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClickLabel = null,
                    role = null,
                    enabled = true,
                    onClick = onClick
                )) {
            Image(painter = painterResource(id = UiUtils.getTypeImg(type)), contentDescription = "billType",
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp))
            Text(text = type, fontSize = 13.sp, modifier = Modifier.padding(top = 5.dp))
        }
    }

    @Composable
    private fun TagsRow(tags: List<String>) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "标签", fontSize = 15.sp, modifier = Modifier.padding(5.dp))
            LazyRow(modifier = Modifier.padding(5.dp)) {
                items(tags) { tag ->
                    Text(text = tag, fontSize = 13.sp,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(3.dp)
                            )
                            .padding(start = 5.dp, end = 5.dp, top = 3.dp, bottom = 3.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null, enabled = true
                            ) {
                                curTags.value += " $tag"
                            })
                }
            }
        }
    }

    @Composable
    private fun BottomRow(account: String) {
        Row(modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier
                .weight(1F)
                .padding(start = 10.dp, end = 10.dp)) {
                Text(text = if (account == "") "不选账户" else account, fontSize = 15.sp, modifier = Modifier.clickable {
                    loading.value = true
                    accountDialogState.value = true
                })
            }
            Text(text = if (curTags.value == "") "备注" else curTags.value.trim(), fontSize = 15.sp, modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
                .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
                .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp))
        }
        if (accountDialogState.value)
            AccountsDialog()
    }

    @Composable
    private fun Calculator() {
        Column(modifier = Modifier.background(Color(0xFF66CCFF))) {
            Row {
                CalCell(cell = "7", modifier = Modifier.weight(1F))
                CalCell(cell = "8", modifier = Modifier.weight(1F))
                CalCell(cell = "9", modifier = Modifier.weight(1F))
                CalCell(cell = "X", modifier = Modifier.weight(1F))
            }
            Row {
                CalCell(cell = "4", modifier = Modifier.weight(1F))
                CalCell(cell = "5", modifier = Modifier.weight(1F))
                CalCell(cell = "6", modifier = Modifier.weight(1F))
                CalCell(cell = "+", modifier = Modifier.weight(1F))
            }
            Row {
                CalCell(cell = "1", modifier = Modifier.weight(1F))
                CalCell(cell = "2", modifier = Modifier.weight(1F))
                CalCell(cell = "3", modifier = Modifier.weight(1F))
                CalCell(cell = "-", modifier = Modifier.weight(1F))
            }
            Row {
                CalCell(cell = "￥", modifier = Modifier.weight(1F))
                CalCell(cell = "0", modifier = Modifier.weight(1F))
                CalCell(cell = ".", modifier = Modifier.weight(1F))
                CalCell(cell = "完成", modifier = Modifier.weight(1F))
            }
        }
    }

    @Composable
    private fun CalCell(cell: String, modifier: Modifier = Modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
            modifier = modifier
                .border(width = 1.dp, color = Color.Gray)
                .clickable { calculate(cell) }) {
            Text(text = cell, modifier = Modifier.padding(10.dp), fontSize = 18.sp, color = Color.White)
        }
    }

    @Composable
    private fun AccountsDialog() {
        val selectedAccount = remember {
            mutableStateOf("")
        }
        val selectedAccountId = remember {
            mutableStateOf(curAccount.value)
        }
        getUserAccounts()
        Dialog(onDismissRequest = { accountDialogState.value = false}) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(10.dp))
                    .padding(top = 20.dp, bottom = 20.dp, start = 10.dp, end = 10.dp)
                    .fillMaxWidth(0.8F)
                    .fillMaxHeight(0.6F)
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .weight(1F)
                        .padding(10.dp)
                ) {
                    if (accountList.size != 0) {
                        items(accountList) { account ->
                            if (account.name != null && account.id != null) {
                                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.padding(top = 10.dp)) {
                                    RadioButton(selected = account.name == selectedAccount.value,
                                        onClick = {
                                            selectedAccount.value = account.name
                                            selectedAccountId.value = account.id
                                        })
                                    Text(
                                        text = account.name,
                                        modifier = Modifier.padding(start = 10.dp),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
                Text(text = "确定", fontSize = 16.sp, color = Color(0xFF66CCFF), modifier = Modifier
                    .background(shape = RoundedCornerShape(10.dp), color = Color.White)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF66CCFF),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable {
                        accountDialogState.value = false
                        curAccountName.value = selectedAccount.value
                        curAccount.value = selectedAccountId.value
                    }
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp))
            }
        }
    }

    private fun getUserAccounts() {
        val user = GlobalVariables.USER
        if (user?.id != null) {
            val userId = user.id
            AccountApi.getAccountsByUserId(userId) { result ->
                loading.value = false
                if (result?.data != null) {
                    accountList.clear()
                    accountList.addAll(result.data)
                }
            }
        }
        else {
            loading.value = false
            Toast.makeText(this@BillSaveActivity, "请先登录", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculate(key: String) {
        if (key.isDigitsOnly() && curBalance.value.length < 7) {
            var balance =
                when {
                    curBalance.value == "" -> 0.0
                    curBalance.value.last() == '.' -> curBalance.value.substring(0, curBalance.value.length-1).toDouble()
                    else -> curBalance.value.toDouble()
                }
            when (curPoint) {
                -1 -> balance = balance * 10 + key.toDouble()
                0 -> {
                    balance += key.toDouble() / 10
                    curPoint += 1
                }
                1 -> {
                    balance += key.toDouble() / 100
                    curPoint += 1
                }
            }
            curBalance.value = if (curPoint == -1) balance.toInt().toString() else balance.toString()
        }
        else if (key == "." && curBalance.value.length < 7) {
            curPoint = 0
            curBalance.value += "."
        }
        else if (key == "X") {
            if (curPoint > -1) {
                curPoint -= 1
            }
            if (curBalance.value != ""){
                curBalance.value = curBalance.value.substring(0, curBalance.value.length-1)
            }
        }
        else if (key == "完成") {
            postBill()
        }
    }

    private fun postBill() {
        loading.value = true
        val balance =
            when (curBalance.value) {
                "" -> 0.0
                else -> curBalance.value.trim('.').toDouble()
            }
        val bill = Bill(null, curAccount.value, balance, GlobalVariables.BOOK?.id?:4L, null,
                curIo.value, curTags.value, curType.value)
        BillApi.postBill(bill) {
            loading.value = false
            if (it != null && it.code == 200) {
                Toast.makeText(this@BillSaveActivity, "创建账单成功", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@BillSaveActivity, MainActivity::class.java)
                intent.putExtra("updateState", true)
                setResult(RESULT_OK, intent)
                finish()
            }
            else {
                Toast.makeText(this@BillSaveActivity, "创建账单失败", Toast.LENGTH_SHORT).show()
            }
        }
    }
}