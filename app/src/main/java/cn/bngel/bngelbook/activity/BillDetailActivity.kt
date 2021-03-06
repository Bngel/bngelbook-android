package cn.bngel.bngelbook.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
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
import cn.bngel.bngelbook.data.bean.Bill
import cn.bngel.bngelbook.network.api.AccountApi
import cn.bngel.bngelbook.network.api.BillApi
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
    private fun BillDetailPage() {
        Column {
            BillDetailTitle()
            BillDetailBill()
            Divider(modifier = Modifier.padding(10.dp))
            BillDetailCard()
        }
    }

    @Composable
    private fun BillDetailTitle() {
        Box(modifier = Modifier.fillMaxWidth()) {
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
                                    Intent(this@BillDetailActivity, MainActivity::class.java)
                                setResult(RESULT_CANCELED, intent)
                                finish()
                            })
                }
                Row(modifier = Modifier.weight(1F), horizontalArrangement = Arrangement.End) {
                    Image(imageVector = Icons.Filled.Delete,
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
                    0 -> "??????"
                    1 -> "??????"
                    else -> ""
                }, fontSize = 18.sp)
            }
        }
    }

    @Composable
    private fun BillDetailBill() {
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
    private fun BillDetailCard() {
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
                BillDetailCardRow(imageVector = Icons.Filled.AccountBox, key = "??????", value = accountName.value)
                BillDetailCardRow(imageVector = Icons.Filled.DateRange, key = "??????", value = bill.value?.createTime?.toString()?:"")
                BillDetailCardRow(imageVector = Icons.Filled.Info, key = "??????", value = bill.value?.tags?:"")
            }
        }
    }

    @Composable
    private fun BillDetailCardRow(imageVector: ImageVector, key: String, value: String) {
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
    private fun DeleteDialog() {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                Text( text = "??????" , fontSize = 15.sp, modifier = Modifier
                    .clickable {
                        deleteBill()
                        deleteState.value = false
                    }
                    .padding(10.dp))},
            dismissButton = {
                Text( text = "??????" , fontSize = 15.sp, modifier = Modifier
                    .clickable {
                        deleteState.value = false
                    }
                    .padding(10.dp))},
            title = { Text( text = "??????:" , fontSize = 15.sp)},
            text = { Text( text = "?????????????????????????", fontSize = 18.sp)}
        )
    }

    private fun deleteBill() {
        if (bill.value != null && bill.value?.id != null) {
            BillApi.deleteBillById(bill.value?.id!!) { result ->
                if (result?.code == 200) {
                    Toast.makeText(this@BillDetailActivity, "??????????????????", Toast.LENGTH_SHORT).show()
                    PageManager.updateAllPage()
                    finish()
                }
                else {
                    Toast.makeText(this@BillDetailActivity, "??????????????????", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
