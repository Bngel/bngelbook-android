package cn.bngel.bngelbook.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.bngel.bngelbook.data.GlobalVariables
import cn.bngel.bngelbook.data.MainPages
import cn.bngel.bngelbook.data.accountDao.Account
import cn.bngel.bngelbook.network.AccountApi
import cn.bngel.bngelbook.ui.widget.UiWidget

object PageAccount: BasePage() {

    private val loading = mutableStateOf(false)
    private val accounts = mutableStateListOf<Account>()

    private val balance = mutableStateOf(0.00)

    init {
        setPage(MainPages.ACCOUNT_PAGE)
    }

    @Composable
    fun AccountPage() {
        Column {
            AccountTitle()
            AccountOverview()
            AccountCard()
        }
        if (loading.value)
            UiWidget.Dialog_Loading()
    }

    @Composable
    fun AccountTitle() {
        Box(modifier = Modifier
            .fillMaxWidth()
            .shadow(1.dp)) {
            Text(text = "我的账户", fontSize = 18.sp, textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp))
        }
    }

    @Composable
    fun AccountOverview() {
        Column {
            Text(text = "净资产", fontSize = 18.sp, textAlign = TextAlign.Start ,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp))
            Text(text = balance.value.toString(), fontSize = 26.sp, textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp))
        }
    }

    @Composable
    fun AccountCard() {
        if (getUpdate()) {
            loading.value = true
            AccountApi.getAccountsByUserId(GlobalVariables.USER?.id ?: 0L) { result ->
                if (result?.data != null) {
                    accounts.clear()
                    accounts.addAll(result.data)
                    balance.value = 0.00
                    accounts.forEach { account ->
                        balance.value += account.balance ?: 0.00
                    }
                    setUpdate(false)
                }
                loading.value = false
            }
        }
        if (accounts.size > 0) {
            Card(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)){
                LazyColumn(modifier = Modifier.padding(10.dp)) {
                    items(accounts) { account ->
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
                            Icon(imageVector = Icons.Filled.AccountBox, contentDescription = null)
                            Text(
                                text = account.name ?: "",
                                fontSize = 18.sp,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 15.dp, end = 5.dp)
                            )
                            Text(
                                text = account.balance?.toString() ?: "", fontSize = 16.sp,
                                modifier = Modifier.padding(start = 5.dp, end = 5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}