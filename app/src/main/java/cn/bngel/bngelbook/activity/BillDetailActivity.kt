package cn.bngel.bngelbook.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.bngel.bngelbook.R
import cn.bngel.bngelbook.data.billDao.Bill
import cn.bngel.bngelbook.network.AccountApi
import cn.bngel.bngelbook.network.BillApi
import cn.bngel.bngelbook.ui.page.PageManager
import cn.bngel.bngelbook.ui.theme.BngelbookTheme

class BillDetailActivity : BaseActivity() {

    private val bill =  mutableStateOf<Bill?>(null)
    private val deleteState = mutableStateOf(false)
    private val accountName = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bill.value = intent.getSerializableExtra("bill") as Bill
        setContent {
            BngelbookTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    BillDetailPage()
                    if (deleteState.value) {
                        DeleteDialog()
                    }
                }
            }
        }
    }

    @Composable
    fun BillDetailPage() {
        Column {
            BillDetailTitle()
            BillDetailBill()
            Divider(modifier = Modifier.padding(10.dp))
            BillDetailCard()
        }
    }

    @Composable
    fun BillDetailTitle() {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.Start) {
                    Image(painter = painterResource(id = R.drawable.close),
                        contentDescription = "close_btn",
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 15.dp)
                            .width(30.dp)
                            .height(30.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                val intent =
                                    Intent(this@BillDetailActivity, MainActivity::class.java)
                                setResult(RESULT_CANCELED, intent)
                                finish()
                            })
                }
                Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.End) {
                    Image(painter = painterResource(id = R.drawable.delete),
                        contentDescription = "delete_btn",
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 15.dp)
                            .width(30.dp)
                            .height(30.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                deleteState.value = true
                            }
                    )
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp), horizontalArrangement = Arrangement.Center) {
                Text(text = when (bill.value?.io) {
                    0 -> "支出"
                    1 -> "收入"
                    else -> ""
                }, fontSize = 18.sp)
            }
        }
    }

    @Composable
    fun BillDetailBill() {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)) {
            Image(painter = painterResource(id = R.drawable.default_profile),
                contentDescription = "bill type",
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp))
            Column(modifier = Modifier
                .weight(1F)
                .align(Alignment.CenterVertically)) {
                Text(text = bill.value?.type?:"", fontSize = 18.sp, textAlign = TextAlign.Start)
                Text(text = if (bill.value?.tags != null) bill.value?.tags?.trim()?:"" else "", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Start)
            }
            Text(text = bill.value?.balance.toString(), fontSize = 20.sp, modifier = Modifier
                .padding(start = 10.dp, end = 15.dp)
                .align(
                    Alignment.CenterVertically
                ))
        }
    }

    @Composable
    fun BillDetailCard() {
        val accountId = bill.value?.accountId
        if (accountId != null) {
            AccountApi.getAccountById(accountId) { result ->
                val account = result?.data
                if (account?.name != null) {
                    accountName.value = account.name
                }
            }
        }
        Card(shape = RoundedCornerShape(10.dp), modifier = Modifier.padding(10.dp),
            border = BorderStroke(1.dp, Color.Gray)) {
            Column(modifier = Modifier.padding(10.dp)) {
                BillDetailCardRow(imageVector = Icons.Filled.AccountBox, key = "账户", value = accountName.value)
                BillDetailCardRow(imageVector = Icons.Filled.DateRange, key = "日期", value = bill.value?.createTime?.toString()?:"")
                BillDetailCardRow(imageVector = Icons.Filled.Info, key = "备注", value = bill.value?.tags?:"")
            }
        }
    }

    @Composable
    fun BillDetailCardRow(imageVector: ImageVector, key: String, value: String) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp), verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.weight(2F), horizontalArrangement = Arrangement.Start
                , verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = imageVector, null, modifier = Modifier.padding(5.dp))
                Text(text = key, fontSize = 14.sp)
            }
            Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.End
                , verticalAlignment = Alignment.CenterVertically) {
                Text(text = value, fontSize = 14.sp)
            }
        }
    }

    @Composable
    fun DeleteDialog() {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Text( text = "确认" , fontSize = 15.sp, modifier = Modifier
                    .clickable {
                        deleteBill()
                        deleteState.value = false
                    }
                    .padding(10.dp))},
            dismissButton = {
                Text( text = "取消" , fontSize = 15.sp, modifier = Modifier
                    .clickable {
                        deleteState.value = false
                    }
                    .padding(10.dp))},
            title = { Text( text = "提示:" , fontSize = 15.sp)},
            text = { Text( text = "是否确定删除账单?", fontSize = 18.sp)}
        )
    }

    private fun deleteBill() {
        if (bill.value != null && bill.value?.id != null) {
            BillApi.deleteBillById(bill.value?.id!!) { result ->
                if (result?.code == 200) {
                    Toast.makeText(this@BillDetailActivity, "删除账单成功", Toast.LENGTH_SHORT).show()
                    PageManager.updateAllPage()
                    finish()
                }
                else {
                    Toast.makeText(this@BillDetailActivity, "删除账单失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
