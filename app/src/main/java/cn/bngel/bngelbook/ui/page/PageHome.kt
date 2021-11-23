package cn.bngel.bngelbook.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.bngel.bngelbook.R
import cn.bngel.bngelbook.data.billDao.Bill
import cn.bngel.bngelbook.network.BillApi
import cn.bngel.bngelbook.ui.widget.UiWidget.Dialog_Loading

object PageHome {

    val billUpdateState = mutableStateOf(true)
    private val loadingBills = mutableStateOf(false)
    private val billList = mutableListOf<Bill>()
    private val curBalance = mutableStateOf(0.0)
    private val curCost = mutableStateOf(0.0)
    private val curIncome = mutableStateOf(0.0)
    private val curBookId = mutableStateOf(4L)

    @Composable
    fun HomePage() {
        Column {
            Home_Overview()
            if (billUpdateState.value)
                updateBills()
            if (loadingBills.value) {
                Dialog_Loading()
            }
            Home_BillList(billList)
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
                Text(text = "13月", modifier = Modifier
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(10.dp))
                    .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp))
            }
            Row{
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)) {
                    Text(text = curBalance.value.toString(), fontSize = 25.sp)
                    Text(text = "13月结余", fontSize = 16.sp)
                }
            }
            Row{
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1F)
                        .padding(10.dp)) {
                    Text(text = curIncome.value.toString(), fontSize = 18.sp)
                    Text(text = "13月收入", fontSize = 13.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1F)
                        .padding(10.dp)) {
                    Text(text = curCost.value.toString(), fontSize = 18.sp)
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
                Text(text = bill.type?:"", fontSize = 18.sp, textAlign = TextAlign.Start)
                Text(text = if (bill.tags!=null) bill.tags.trim() else "", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Start)
            }
            Text(text = bill.balance.toString(), fontSize = 20.sp, modifier = Modifier
                .padding(start = 10.dp, end = 15.dp)
                .align(
                    Alignment.CenterVertically
                ))
        }
    }

    private fun updateBills() {
        loadingBills.value = true
        BillApi.getBillsByBookId(curBookId.value) { bills ->
            if (bills != null) {
                if (bills.code == 200)
                    billUpdateState.value = false
                if (bills.data != null) {
                    billList.clear()
                    val data = bills.data
                    var totalIncome = 0.0
                    var totalCost = 0.0
                    for (bill in data) {
                        if (bill.balance != null) {
                            if (bill.io == 1) {
                                totalIncome += bill.balance
                            }
                            else {
                                totalCost += bill.balance
                            }
                        }
                    }
                    curIncome.value = totalIncome
                    curCost.value = totalCost
                    curBalance.value = totalIncome - totalCost
                    billList.addAll(data)
                }
            }
            loadingBills.value = false
        }
    }

}