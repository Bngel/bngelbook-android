package cn.bngel.bngelbook.ui.page

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.bngel.bngelbook.R
import cn.bngel.bngelbook.data.billDao.Bill
import cn.bngel.bngelbook.network.BillApi
import cn.bngel.bngelbook.ui.widget.UiWidget.Dialog_Loading
import java.sql.Date

object HomePage {

    @Composable
    fun HomePage() {
        Column {
            Home_Overview()
            val billList = remember {
                mutableListOf<Bill>()
            }
            var loadingBills = remember {
                mutableStateOf(true)
            }
            if (loadingBills.value) {
                Dialog_Loading()
            }
            BillApi.getBillsByAccountId(1L) { bills ->
                Log.d("bngelbook_bill", bills.toString())
                if (bills != null) {
                    if (bills.data != null) {
                        val data = bills.data
                        billList.addAll(data)
                    }
                }
                loadingBills.value = false
            }
            Home_BillList(bills = billList)
        }
    }

    @Composable
    fun Home_Overview() {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF66CCFF))) {
            Row(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 15.dp)){
                Text(text = "13月")
            }
            Row{
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)) {
                    Text(text = "1313.13", fontSize = 25.sp)
                    Text(text = "13月结余", fontSize = 16.sp)
                }
            }
            Row{
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1F)
                        .padding(10.dp)) {
                    Text(text = "1313.13", fontSize = 18.sp)
                    Text(text = "13月收入", fontSize = 13.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1F)
                        .padding(10.dp)) {
                    Text(text = "1313.13", fontSize = 18.sp)
                    Text(text = "13月支出", fontSize = 13.sp)
                }
            }

        }
    }

    @Composable
    fun Home_BillList(bills: List<Bill>) {
        LazyColumn {
            items(bills) { bill ->
                Home_Bill(bill)
            }
        }
    }

    @Composable
    fun Home_Bill(bill: Bill) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)) {
            Image(painter = painterResource(id = R.drawable.default_profile),
                contentDescription = "bill type",
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp))
            Column(modifier = Modifier
                .weight(1F)
                .align(Alignment.CenterVertically)) {
                Text(text = bill.type, fontSize = 18.sp)
                Text(text = bill.tags?:"", fontSize = 12.sp)
            }
            Text(text = bill.balance.toString(), fontSize = 20.sp, modifier = Modifier
                .padding(start = 10.dp, end = 15.dp)
                .align(
                    Alignment.CenterVertically
                ))
        }
    }
}